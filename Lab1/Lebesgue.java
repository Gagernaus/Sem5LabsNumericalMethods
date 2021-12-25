package Lab1;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class Lebesgue {

    public static double[] makeKnots(double left, double right, int amount) {
        double[] res = new double[amount];
        double step = (right - left) / (amount + 1);
        for (int i = 1; i < amount + 1; i++) {
            res[i - 1] = left + step * i;
        }
        return res;
    }

    public static double lagrangeI(double x, int i, double[] knots) {
        double res = 1;
        for (int j = 0; j < knots.length; ++j) {
            if (i != j)
                res *= (x - knots[j]) / (knots[i] - knots[j]);
        }
        return res;
    }

    public static double lebesgue(double x, double[] knots) {
        double res = 0;
        for (int i = 0; i < knots.length; ++i) {
            res += Math.abs(lagrangeI(x, i, knots));
        }
        return res;
    }

    public static double maximizeLebesgue(double left, double right, double step, int amount) {
        double res = 0;
        double[] knots = makeKnots(left, right, amount);
        for (double x = left; x < right; x += step) {
            double current = lebesgue(x, knots);
            if (res == 0 || current > res) {
                res = current;
            }
        }
        return res;
    }

    public static double lowerBorder(int amount) {
        return (Math.pow(2, amount)) / (8 * Math.pow(amount, 1.5));
    }

    public static double upperBorder(int amount) {
        return Math.pow(2, amount - 1);
    }

    public static void main(String[] args) {
        int maxAmount = 100;
        double left = 0;
        double right = 10;
        double step = 0.01;
        XYSeriesCollection data = new XYSeriesCollection();
        XYSeries seriesLow = new XYSeries("Lower Border");
        XYSeries seriesLeb = new XYSeries("Lebesgue");
        XYSeries seriesUp = new XYSeries("Upper Border");
        for (int n = 1; n <= maxAmount; ++n) {
            seriesLow.add(n, lowerBorder(n));
            seriesUp.add(n, upperBorder(n));
            seriesLeb.add(n, maximizeLebesgue(left, right, step, n));
        }
        data.addSeries(seriesLow);
        data.addSeries(seriesLeb);
        data.addSeries(seriesUp);
        XYDataset dataset = data;
        JFreeChart chart = ChartFactory.createXYLineChart("Chart", "x", "y", dataset);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.GREEN);
        renderer.setSeriesPaint(2, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(4.0f));
        renderer.setSeriesStroke(1, new BasicStroke(4.0f));
        renderer.setSeriesStroke(2, new BasicStroke(4.0f));
        plot.setOutlinePaint(Color.BLACK);
        plot.setOutlineStroke(new BasicStroke(2.0f));
        plot.setRenderer(renderer);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.MAGENTA);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.MAGENTA);
        JFrame frame = new JFrame("Frame");
        ChartPanel panel = new ChartPanel(chart);
        frame.getContentPane().add(panel);
        frame.setSize(640, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.show();
    }
}
