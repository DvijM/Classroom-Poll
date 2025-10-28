import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class ResultPanel extends JFrame {

    private Poll poll;

    public ResultPanel(Poll poll) {
        this.poll = poll;
        setTitle("Results for: " + poll.getQuestion());
        setSize(800, 600);
        setLocationRelativeTo(null); // Center

        // Create Chart
        CategoryChart chart = createChart();

        // Show it
        JPanel chartPanel = new XChartPanel<>(chart);
        add(chartPanel);

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
}