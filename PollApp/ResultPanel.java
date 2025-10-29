import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class ResultPanel extends JFrame {

    private Poll poll;
    private CategoryChart chart;

    public ResultPanel(Poll poll) {
        this.poll = poll;
        setTitle("Results for: " + poll.getQuestion());
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        
        this.chart = createChart();

        // Show it
        JPanel chartPanel = new XChartPanel<>(chart);
        add(chartPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton exportButton = new JButton("Save as PNG");

        exportButton.addActionListener(e -> {
            saveChartAsPNG();
        });

        buttonPanel.add(exportButton);
        add(buttonPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    private CategoryChart createChart() {
        // Fetch the vote counts from the database
        Map<String, Integer> voteCounts = DatabaseHelper.getVoteCounts(poll);

        // Create Chart
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("Poll Results")
                .xAxisTitle("Options")
                .yAxisTitle("Number of Votes")
                .build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        //chart.getStyler().setHasAnnotations(true); // Show values on top of bars

        // Add Series
        chart.addSeries("Votes",
                new ArrayList<>(voteCounts.keySet()),
                new ArrayList<>(voteCounts.values()));

        return chart;
    }
    
    private void saveChartAsPNG() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Chart as PNG");
        
        // Suggest a filename
        String saneFilename = poll.getQuestion().replaceAll("[^a-zA-Z0-9.-]", "_") + ".png";
        fileChooser.setSelectedFile(new File(saneFilename));

        // Filter for PNG files
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            // Ensure it has a .png extension
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".png")) {
                fileToSave = new File(filePath + ".png");
            }

            try {
                BitmapEncoder.saveBitmap(this.chart, fileToSave.getAbsolutePath(), BitmapFormat.PNG);
                JOptionPane.showMessageDialog(this, "Chart saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving chart: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
