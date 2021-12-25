package Lab1;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.PlotOrientation;

import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

    static double f(double x) {
        return Math.sin(Math.exp(x / 3) / 10);
    }

    static double lograngePolynomial(int amount, double[] knots, double[] fis, double x) {
        double sum = 0;
        for (int i = 0; i < amount; ++i) {
            double mult = 1;
            for (int j = 0; j < amount; ++j) {
                if (j != i) {
                    mult = mult * ((x - knots[j]) / (knots[i] - knots[j]));
                }
            }
            sum += fis[i] * mult;
        }
        return sum;
    }

    public static double getPoly(double[] knots, double x, int i) {
        double mult = 1;
        for (int j = 0; j < knots.length; ++j) {
            if (i != j) {
                mult = mult * (x - knots[j]) / (knots[i] - knots[j]);
            }
        }
        return mult;
    }


    public static void main(String[] args) {
        int left = 0;
        int right = 10;
        double step = 0.5;
        int amount = (int) Math.round((right - left) / step) + 1;
        System.out.println(amount);
        double[] knots = new double[amount];
        double[] fis = new double[amount];
        double[] points = new double[amount - 1];
        double[] fs = new double[amount - 1];
        XYSeries xySeries1 = new XYSeries("Fi(xi)");
        for (int i = 0; i < amount; ++i) {
            knots[i] = left + i * step;
            fis[i] = f(knots[i]);
            System.out.println(knots[i] + " " + fis[i]);
            xySeries1.add(knots[i], fis[i]);
        }
        XYDataset data1 = new XYSeriesCollection(xySeries1);
        JFreeChart chart1 = ChartFactory.createXYLineChart("Fi(xi)", "x", "y",
                data1, PlotOrientation.VERTICAL, true, true, false);
        System.out.print("\n");
        XYSeries xySeries2 = new XYSeries("F(x)");
        for (int i = 0; i < amount - 1; ++i) {
            points[i] = knots[i] + (knots[i + 1] - knots[i]) / 2;
            fs[i] = lograngePolynomial(amount, knots, fis, points[i]);
            System.out.println(points[i] + " " + fs[i]);
            xySeries2.add(points[i], fs[i]);
        }
        JFrame frame1 = new JFrame("Chart1");
        frame1.getContentPane().add(new ChartPanel(chart1));
        frame1.setSize(new java.awt.Dimension(450, 450));
        frame1.show();
        System.out.print("\n");
        XYDataset data2 = new XYSeriesCollection(xySeries2);
        JFreeChart chart2 = ChartFactory.createXYLineChart("F(x)", "x", "y",
                data2, PlotOrientation.VERTICAL, true, true, false);
        JFrame frame2 = new JFrame("Chart2");
        frame2.getContentPane().add(new ChartPanel(chart2));
        frame2.setSize(new java.awt.Dimension(450, 450));
        frame2.show();
        for (double i = 0; i < 10; i += 0.5) {
            System.out.println(i + " " + (f(i) - lograngePolynomial(amount, knots, fis, i)));
        }
        XYSeries xySeries3 = new XYSeries("F(x)-Fi(x)");
        for (double i = 0; i <= 10; i += 0.25) {
            xySeries3.add(i, f(i) - lograngePolynomial(amount, knots, fis, i));
            System.out.println(i);
        }
        XYDataset data3 = new XYSeriesCollection(xySeries3);
        JFreeChart chart3 = ChartFactory.createXYLineChart("F(x)-Fi(x)", "x", "y",
                data3, PlotOrientation.VERTICAL, true, true, false);
        JFrame frame3 = new JFrame("Chart3");
        frame3.getContentPane().add(new ChartPanel(chart3));
        frame3.setSize(new java.awt.Dimension(450, 450));
        frame3.show();
//        ArrayList list = new ArrayList<>();
//        for (int j = 0; j < points.length; ++j) {
//            double sum = 0;
//            for (int i = 0; i < knots.length; ++i) {
//                sum += Math.abs(getPoly(knots, points[j], i));
//            }
//            list.add(sum);
//            System.out.println(sum + " " + points[j]);
//        }
//        System.out.println(Collections.max(list));
    }
}
