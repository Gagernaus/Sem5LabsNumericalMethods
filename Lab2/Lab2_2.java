package Lab2;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class Lab2_2 {
    public static double f(double x) {
        return Math.atan(Math.sin(x));
    }

    public static double derivativeF(double x) {
        return Math.cos(x) / (1 + Math.pow(Math.sin(x), 2));
    }

    public static double[] makeKnots(double left, double right, int amount) {
        double[] res = new double[amount + 2];
        double step = (right - left) / (amount + 1);
        res[0] = left;
        for (int i = 1; i < amount + 1; i++) {
            res[i] = left + step * i;
        }
        res[amount + 1] = right;
        return res;
    }

    public static double secondDerivativeF(double x) {
        return -Math.sin(x) * (2 + Math.pow(Math.cos(x), 2)) / Math.pow((1 + Math.pow(Math.sin(x), 2)), 2);
    }

    public static double estimatedSecondDerivative(double x, double step) {
        return (-f(x + 2 * step) + 16 * f(x + step) - 30 * f(x) + 16 * f(x - step) - f(x - 2 * step)) / (12 * Math.pow(step, 2));
    }

    public static void main(String[] args) {
        double left = -3;
        double right = 3;
        int amount = 100;
        double step = (right - left) / (amount + 1);
        System.out.println(step);
        double[] knots = makeKnots(left, right, amount);
        double[] correctDivergence = new double[knots.length];
        double[] estimatedDivergence = new double[knots.length];
        XYSeriesCollection data1 = new XYSeriesCollection();
        XYSeries correct = new XYSeries("Correct derivative");
        XYSeries estimated = new XYSeries("Estimated derivative");
        XYSeries difference = new XYSeries("Difference");
        for (int i = 0; i < knots.length; ++i) {
            correctDivergence[i] = derivativeF(knots[i]);
            if (i == 0) {
                estimatedDivergence[i] = (-3 * f(knots[i]) + 4 * f(knots[i + 1]) - f(knots[i + 2])) / (2 * step);
            } else if (i == knots.length - 1) {
                estimatedDivergence[i] = (f(knots[i - 2]) - 4 * f(knots[i - 1]) + 3 * f(knots[i])) / (2 * step);
            } else {
                estimatedDivergence[i] = (f(knots[i + 1]) - f(knots[i - 1])) / (step * 2);
            }
            difference.add(knots[i], Math.abs(correctDivergence[i] - estimatedDivergence[i]));
            correct.add(knots[i], correctDivergence[i]);
            estimated.add(knots[i], estimatedDivergence[i]);
        }

        double[] correct2ndDivergence = new double[knots.length];
        double[] estimated2ndDivergence = new double[knots.length];
        XYSeriesCollection data3 = new XYSeriesCollection();
        XYSeries correct2 = new XYSeries("Correct derivative 2");
        XYSeries estimated2 = new XYSeries("Estimated derivative 2");
        XYSeries difference2 = new XYSeries("Difference 2");
        for (int i = 0; i < knots.length; ++i) {
            correct2ndDivergence[i] = secondDerivativeF(knots[i]);
            estimated2ndDivergence[i] = estimatedSecondDerivative(knots[i], step);
            correct2.add(knots[i], correct2ndDivergence[i]);
            estimated2.add(knots[i], estimated2ndDivergence[i]);
            difference2.add(knots[i], Math.abs(correct2ndDivergence[i] - estimated2ndDivergence[i]));
        }

        data1.addSeries(correct);
        data1.addSeries(estimated);
        XYDataset dataset1 = data1;
        XYDataset dataset2 = new XYSeriesCollection(difference);
        JFreeChart chart1 = ChartFactory.createXYLineChart("Divergence", "x", "y", dataset1);
        XYPlot plot1 = chart1.getXYPlot();
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
        renderer1.setSeriesPaint(0, Color.RED);
        renderer1.setSeriesPaint(1, Color.BLUE);
        renderer1.setSeriesStroke(0, new BasicStroke(1.0f));
        renderer1.setSeriesStroke(1, new BasicStroke(4.0f));
        plot1.setOutlinePaint(Color.BLACK);
        plot1.setOutlineStroke(new BasicStroke(2.0f));
        plot1.setRenderer(renderer1);
        plot1.setRangeGridlinesVisible(true);
        plot1.setRangeGridlinePaint(Color.MAGENTA);
        plot1.setDomainGridlinesVisible(true);
        plot1.setDomainGridlinePaint(Color.MAGENTA);
        JFrame frame1 = new JFrame("Frame1");
        ChartPanel panel1 = new ChartPanel(chart1);
        frame1.getContentPane().add(panel1);
        frame1.setSize(640, 640);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.show();
        JFreeChart chart2 = ChartFactory.createXYLineChart("Difference", "x", "y", dataset2);
        XYPlot plot2 = chart2.getXYPlot();
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        renderer2.setSeriesPaint(0, Color.GREEN);
        renderer2.setSeriesStroke(0, new BasicStroke(2.0f));
        plot2.setOutlinePaint(Color.BLACK);
        plot2.setOutlineStroke(new BasicStroke(2.0f));
        plot2.setRenderer(renderer2);
        plot2.setRangeGridlinesVisible(true);
        plot2.setRangeGridlinePaint(Color.MAGENTA);
        plot2.setDomainGridlinesVisible(true);
        plot2.setDomainGridlinePaint(Color.MAGENTA);
        JFrame frame2 = new JFrame("Frame2");
        ChartPanel panel2 = new ChartPanel(chart2);
        frame2.getContentPane().add(panel2);
        frame2.setSize(640, 640);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.show();

        data3.addSeries(correct2);
        data3.addSeries(estimated2);
        XYDataset dataset3 = data3;
        XYDataset dataset4 = new XYSeriesCollection(difference2);
        JFreeChart chart3 = ChartFactory.createXYLineChart("2nd Divergence", "x", "y", dataset3);
        XYPlot plot3 = chart3.getXYPlot();
        XYLineAndShapeRenderer renderer3 = new XYLineAndShapeRenderer();
        renderer3.setSeriesPaint(0, Color.RED);
        renderer3.setSeriesPaint(1, Color.BLUE);
        renderer3.setSeriesStroke(0, new BasicStroke(1.0f));
        renderer3.setSeriesStroke(1, new BasicStroke(1.0f));
        plot3.setOutlinePaint(Color.BLACK);
        plot3.setOutlineStroke(new BasicStroke(2.0f));
        plot3.setRenderer(renderer3);
        plot3.setRangeGridlinesVisible(true);
        plot3.setRangeGridlinePaint(Color.MAGENTA);
        plot3.setDomainGridlinesVisible(true);
        plot3.setDomainGridlinePaint(Color.MAGENTA);
        JFrame frame3 = new JFrame("Frame3");
        ChartPanel panel3 = new ChartPanel(chart3);
        frame3.getContentPane().add(panel3);
        frame3.setSize(640, 640);
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame3.show();
        JFreeChart chart4 = ChartFactory.createXYLineChart("2nd Difference", "x", "y", dataset4);
        XYPlot plot4 = chart4.getXYPlot();
        XYLineAndShapeRenderer renderer4 = new XYLineAndShapeRenderer();
        renderer4.setSeriesPaint(0, Color.GREEN);
        renderer4.setSeriesStroke(0, new BasicStroke(2.0f));
        plot4.setOutlinePaint(Color.BLACK);
        plot4.setOutlineStroke(new BasicStroke(2.0f));
        plot4.setRenderer(renderer4);
        plot4.setRangeGridlinesVisible(true);
        plot4.setRangeGridlinePaint(Color.MAGENTA);
        plot4.setDomainGridlinesVisible(true);
        plot4.setDomainGridlinePaint(Color.MAGENTA);
        JFrame frame4 = new JFrame("Frame24");
        ChartPanel panel4 = new ChartPanel(chart4);
        frame4.getContentPane().add(panel4);
        frame4.setSize(640, 640);
        frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame4.show();
    }
}
