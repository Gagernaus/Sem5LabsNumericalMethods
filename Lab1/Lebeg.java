package Lab1;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class Lebeg {
//    static double cheb(int n, int k) {
//        return 5 + 5 * Math.cos((2 * k - 1) * Math.PI / (2 * n));
//    }

    static double lebeg(double x, double[] knots, int amount) {
        double z = 0;
        for (int i = 0; i < amount; i++) {
            double z1 = 1;
            for (int j = 0; j < amount; j++) {
                if (j != i)
                    z1 = z1 * (x - knots[j]) / (knots[i] - knots[j]);

            }
            z += z1;
        }
        return z;
    }

    public static double[] makeKnots(double left, double right, double step, int amount) {
        double[] result = new double[amount];
        for (int i = 0; i < amount; ++i) {
            result[i] = left + i * step;
        }
        return result;
    }

    public static double[] makePoints(double[] knots) {
        double[] result = new double[knots.length - 1];
        for (int i = 0; i < knots.length - 1; ++i) {
            result[i] = knots[i] + (knots[i + 1] - knots[i]) / 2;
        }
        return result;
    }

    public static void main(String[] args) {
        double left = 0;
        double right = 10;
        int maxAmount = 10;
        XYSeries xySeries = new XYSeries("Lebeg(n)");
        XYSeries xySeries1 = new XYSeries("Lower Border(n)");
        XYSeries xySeries2 = new XYSeries("Upper border(n)");
//        double step = 0.5;
//        int amount = (int) Math.round((right - left) / step) + 1;
        for (int amount = 10; amount <= maxAmount; amount += 5) {
            double step = (right - left) / amount;
            double[] knots = makeKnots(left, right, step, amount);
            double[] points = makePoints(knots);
            double max = Math.abs(lebeg(points[0], knots, amount));
            for (int i = 0; i < points.length; ++i) {
                max = Math.max(lebeg(points[i], knots, amount), max);
            }
            xySeries.add(amount, max);
            xySeries1.add(amount, (Math.pow(2, amount) / (8 * Math.pow(amount, 1.5))));
            xySeries2.add(amount, Math.pow(2, amount - 1));
        }
        XYDataset data = new XYSeriesCollection(xySeries);
        XYDataset data1 = new XYSeriesCollection(xySeries1);
        XYDataset data2 = new XYSeriesCollection(xySeries2);
        JFreeChart chart = ChartFactory.createXYLineChart("Lebeg(n)", "x", "y",
                data, PlotOrientation.VERTICAL, true, true, false);
        JFreeChart chart1 = ChartFactory.createXYLineChart("Lower Border", "x", "y",
                data1, PlotOrientation.VERTICAL, true, true, false);
        JFreeChart chart2 = ChartFactory.createXYLineChart("Upper border", "x", "y",
                data2, PlotOrientation.VERTICAL, true, true, false);
        JFrame frame = new JFrame("Chart");
        JFrame frame1 = new JFrame("Charts1");
        JFrame frame2 = new JFrame("Chart2");
        frame.getContentPane().add(new ChartPanel(chart));
        frame1.getContentPane().add(new ChartPanel(chart1));
        frame2.getContentPane().add(new ChartPanel(chart2));
        frame.setSize(new java.awt.Dimension(450, 450));
        frame.show();
        frame1.setSize(new java.awt.Dimension(450, 450));
        frame1.show();
        frame2.setSize(new java.awt.Dimension(450, 450));
        frame2.show();

    }
//        int b = 10;
//        int n = 200;
//        double[] knots = new double[n];
//        for (int i = 0; i < n; i++) {
//            knots[i] = cheb(n, i + 1);
//        }
//        double[] points = new double[n + 1];
//        points[0] = knots[0] / 2;
//        for (int i = 1; i < n; i++) {
//            points[i] = knots[i - 1] + (knots[i] - knots[i - 1]) / 2;
//        }
//        points[n] = knots[n - 1] + (b - knots[n - 1]) / 2;
//        double max = lebeg(points[0], knots, n);
//        for (int i = 1; i < n + 1; i++) {
//            max = Math.max(lebeg(points[i], knots, n), max);
//        }
//        System.out.println(max);
//    }
}
