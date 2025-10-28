import javax.swing.*;
import java.awt.*;

public class StudentPanel extends JFrame {

    private Poll activePoll;
    private ButtonGroup optionsGroup;
    private JRadioButton opt1, opt2, opt3, opt4;
    private JButton submitButton;

    public StudentPanel() {
        setTitle("Student Voting Panel");
        setSize(500, 400);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // Try to load the active poll
        activePoll = DatabaseHelper.getActivePoll();

        if (activePoll == null) {
            // No active poll
            JLabel noPollLabel = new JLabel("There is no active poll at this time.", SwingConstants.CENTER);
            noPollLabel.setFont(new Font("Arial", Font.BOLD, 18));
            add(noPollLabel, BorderLayout.CENTER);
        } else {
            // An active poll exists, build the UI
            JLabel questionLabel = new JLabel(
                    "<html><body style='width: 350px;'>" + activePoll.getQuestion() + "</body></html>");
            questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
            questionLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
            add(questionLabel, BorderLayout.NORTH);

            JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
            optionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

            optionsGroup = new ButtonGroup();
            opt1 = new JRadioButton(activePoll.getOption1());
            opt2 = new JRadioButton(activePoll.getOption2());
            opt3 = new JRadioButton(activePoll.getOption3());
            opt4 = new JRadioButton(activePoll.getOption4());

            Font optionFont = new Font("Arial", Font.PLAIN, 16);
            opt1.setFont(optionFont);
            opt2.setFont(optionFont);
            opt3.setFont(optionFont);
            opt4.setFont(optionFont);

            optionsGroup.add(opt1);
            optionsGroup.add(opt2);
            optionsGroup.add(opt3);
            optionsGroup.add(opt4);

            optionsPanel.add(opt1);
            optionsPanel.add(opt2);
            optionsPanel.add(opt3);
            optionsPanel.add(opt4);

            add(optionsPanel, BorderLayout.CENTER);

            submitButton = new JButton("Submit Vote");
            submitButton.setFont(new Font("Arial", Font.BOLD, 18));
            submitButton.addActionListener(e -> submitVote());

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            buttonPanel.add(submitButton);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        setVisible(true);
    }

    private void submitVote() {
        int selectedOption = 0;
        if (opt1.isSelected())
            selectedOption = 1;
        else if (opt2.isSelected())
            selectedOption = 2;
        else if (opt3.isSelected())
            selectedOption = 3;
        else if (opt4.isSelected())
            selectedOption = 4;

        if (selectedOption == 0) {
            JOptionPane.showMessageDialog(this, "Please select an option.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = DatabaseHelper.submitVote(activePoll.getId(), selectedOption);

        if (success) {
            // Fulfill "vote once per poll" by disabling UI after voting
            JOptionPane.showMessageDialog(this, "Vote recorded! Thank you.");
            submitButton.setEnabled(false);
            opt1.setEnabled(false);
            opt2.setEnabled(false);
            opt3.setEnabled(false);
            opt4.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Error submitting vote.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}