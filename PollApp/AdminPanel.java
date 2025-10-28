import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminPanel extends JFrame {

    private JList<Poll> pollList;
    private DefaultListModel<Poll> listModel;

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(700, 500);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null); // Center the window

        // Panel for creating new polls
        add(createNewPollPanel(), BorderLayout.NORTH);

        // Panel for listing existing polls
        add(createPollListPanel(), BorderLayout.CENTER);

        // Panel for control buttons
        add(createControlPanel(), BorderLayout.SOUTH);

        setVisible(true);
        loadPolls(); // Load polls from DB on open
    }

    private JPanel createNewPollPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Create New Poll"));

        panel.add(new JLabel("Question:"));
        JTextField qField = new JTextField();
        panel.add(qField);

        panel.add(new JLabel("Option 1:"));
        JTextField o1Field = new JTextField();
        panel.add(o1Field);

        panel.add(new JLabel("Option 2:"));
        JTextField o2Field = new JTextField();
        panel.add(o2Field);

        panel.add(new JLabel("Option 3:"));
        JTextField o3Field = new JTextField();
        panel.add(o3Field);

        panel.add(new JLabel("Option 4:"));
        JTextField o4Field = new JTextField();
        panel.add(o4Field);

        JButton createButton = new JButton("Create Poll");
        createButton.addActionListener(e -> {
            String q = qField.getText();
            String o1 = o1Field.getText();
            String o2 = o2Field.getText();
            String o3 = o3Field.getText();
            String o4 = o4Field.getText();

            if (q.isEmpty() || o1.isEmpty() || o2.isEmpty() || o3.isEmpty() || o4.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (DatabaseHelper.createPoll(q, o1, o2, o3, o4)) {
                JOptionPane.showMessageDialog(this, "Poll created successfully!");
                loadPolls(); // Refresh the list
                qField.setText("");
                o1Field.setText("");
                o2Field.setText("");
                o3Field.setText("");
                o4Field.setText("");
            }
            else {
                JOptionPane.showMessageDialog(this, "Failed to create poll.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add a 6th row for the button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(createButton);

        // Main panel to hold fields and button
        JPanel mainCreatePanel = new JPanel(new BorderLayout());
        mainCreatePanel.add(panel, BorderLayout.CENTER);
        mainCreatePanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainCreatePanel;
    }

    private JScrollPane createPollListPanel() {
        listModel = new DefaultListModel<>();
        pollList = new JList<>(listModel);
        pollList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pollList.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(pollList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Existing Polls"));
        return scrollPane;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton launchButton = new JButton("Launch Selected Poll");
        launchButton.addActionListener(e -> launchSelectedPoll());

        JButton endButton = new JButton("End Active Poll");
        endButton.addActionListener(e -> endActivePoll());

        JButton resultsButton = new JButton("View Results for Selected");
        resultsButton.addActionListener(e -> viewSelectedResults());

        panel.add(launchButton);
        panel.add(endButton);
        panel.add(resultsButton);
        return panel;
    }

    private void loadPolls() {
        listModel.clear();
        List<Poll> polls = DatabaseHelper.getAllPolls();
        for (Poll poll : polls) {
            listModel.addElement(poll);
        }
    }

    private void launchSelectedPoll() {
        Poll selectedPoll = pollList.getSelectedValue();
        if (selectedPoll == null) {
            JOptionPane.showMessageDialog(this, "Please select a poll to launch.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        DatabaseHelper.launchPoll(selectedPoll.getId());
        JOptionPane.showMessageDialog(this, "Poll launched!");
        loadPolls(); // Refresh list to show "ACTIVE" status
    }

    private void endActivePoll() {
        DatabaseHelper.endPoll();
        JOptionPane.showMessageDialog(this, "All polls ended.");
        loadPolls(); // Refresh list
    }

    private void viewSelectedResults() {
        Poll selectedPoll = pollList.getSelectedValue();
        if (selectedPoll == null) {
            JOptionPane.showMessageDialog(this, "Please select a poll to view results.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Open the ResultPanel for the selected poll
        new ResultPanel(selectedPoll);
    }
}