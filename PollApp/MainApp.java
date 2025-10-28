import javax.swing.*;
import java.awt.*;

public class MainApp {

    public static void main(String[] args) {
        // Ensure database tables are ready before starting
        DatabaseHelper.initializeDatabase();

        // Run the main launcher window
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Poll App Launcher");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLayout(new GridLayout(1, 2, 20, 20));
            frame.setLocationRelativeTo(null);

            JButton adminButton = new JButton("Open Admin Panel");
            adminButton.setFont(new Font("Arial", Font.BOLD, 14));
            adminButton.addActionListener(e -> new AdminPanel());

            JButton studentButton = new JButton("Open Student Panel");
            studentButton.setFont(new Font("Arial", Font.BOLD, 14));
            studentButton.addActionListener(e -> new StudentPanel());

            frame.add(adminButton);
            frame.add(studentButton);
            frame.setVisible(true);
        });
    }
}