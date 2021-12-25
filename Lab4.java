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
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Lab4 {

    public static double f(double x) {
        return Math.exp(Math.sin(x / 2)) - Math.atan(x) + 1;
    }

    public static double derivativeF(double x) {
        return Math.cos(x / 2) * Math.exp(Math.sin(x / 2)) / 2 - 1 / (Math.pow(x, 2) + 1);
    }

    public static double dichotomy(double left, double right, double eps) {
        double result = 0;
        int amount = 0;
        double a = left;
        double b = right;
        if (Math.signum(f(a)) == Math.signum(f(b))) {
            return left - 99999;
        }
        if (f(a) == 0) return a;
        if (f(b) == 0) return b;
        while (Math.abs(a - b) > eps) {
            amount++;
            double c = (a + b) / 2;
            if (f(c) == 0) {
                result = c;
                break;
            } else {
                if (Math.signum(f(a)) != Math.signum(f(c))) {
                    b = c;
                } else {
                    a = c;
                }
            }
            if (Math.abs(b) < Math.abs(a + eps)) {
                result = b;
            } else result = a;
        }
        System.out.println(amount + " iterations");
        return result;
    }

//    public static double newton(double left, double right, double eps) {
//        double previous = (left + right) / 2;
//        double current = previous - f(previous) / derivativeF(previous);
//        while (Math.abs(f(current)) > eps) {
//            previous = current;
//            current = previous - f(previous) / derivativeF(previous);
//        }
//        return current;
//    }

    public static double newton(double left, double right, double eps) {
        double guess = (left + right) / 2;
        int amount = 0;
        if (f(guess) == 0) return guess;
        if (derivativeF(guess) == 0) guess += eps;
        double previous = guess;
        double current = guess;
        do {
            amount++;
            previous = current;
            current = previous - f(previous) / derivativeF(previous);
            while (current < left || current > right) {
                current = (previous + current) / 2;
            }
        } while (Math.abs(current - previous) > eps);
        System.out.println(amount + " Iterations");
        return current;
    }

//    public static double newtonSpeed(double root, double x0, double eps) {
//        double r = 0;
//        while (derivativeF(x0) == 0) {
//            x0 += eps;
//        }
//        double x1 = x0 - f(x0) / derivativeF(x0);
//        if (root - x1 != 0) {
//            r = Math.log(Math.abs(root - x1)) / Math.log(Math.abs(root - x0));
//            double t = newtonSpeed(root, x1, eps);
//            if (t != 0) {
//                return t;
//            }
//        }
//        return r;
//    }

    public static double newtonSpeed(double left, double right, double eps) {
        double root = newton(left, right, eps);
        double r = 0;
        double x0 = (left + right) / 2;
        while (derivativeF(x0) == 0) x0 += eps;
        double x1 = x0 - f(x0) / derivativeF(x0);
        while (root - x1 != 0) {
            r = Math.log(Math.abs(root - x1)) / Math.log(Math.abs(root - x0));
            x0 = x1;
            while (derivativeF(x0) == 0) x0 += eps;
            x1 = x0 - f(x0) / derivativeF(x0);
        }
        return r;
    }

    public static double secant(double left, double right, double eps) {
        double prev = right;
        double curr = left;
        double next;
        int amount = 0;
        while (true) {
            amount++;
            next = curr - f(curr) * (curr - prev) / (f(curr) - f(prev));
            if (Math.abs(next - curr) < eps) {
                System.out.println(amount + " iterations");
                return next;
            } else {
                prev = curr;
                curr = next;
            }
        }
    }

    public static void main(String[] args) {
        double eps1 = Math.pow(10, -3);
        double eps2 = Math.pow(10, -6);
        double eps3 = Math.pow(10, -9);
        double left = 0;
        double right = 10;
        double step = 0.01;
        double[] x = new double[(int) (Math.round(right - left) / step) + 1];
        double[] y = new double[x.length];
        XYSeriesCollection data = new XYSeriesCollection();
        XYSeries function = new XYSeries("function");
        XYSeries zero = new XYSeries("zero");
        for (int i = 0; i < x.length; ++i) {
            x[i] = left + step * i;
            y[i] = f(x[i]);
            function.add(x[i], y[i]);
            zero.add(x[i], 0);
        }
        data.addSeries(function);
        data.addSeries(zero);
        XYDataset dataset = data;
        JFreeChart chart = ChartFactory.createXYLineChart("Graph", "X", "F(X)", dataset);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(0.5f));
        renderer.setSeriesStroke(1, new BasicStroke(0.5f));
        plot.setOutlinePaint(Color.BLACK);
        plot.setOutlineStroke(new BasicStroke(2.0f));
        plot.setRenderer(renderer);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.MAGENTA);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.MAGENTA);
        JFrame frame = new JFrame("Function graph");
        ChartPanel panel = new ChartPanel(chart);
        frame.getContentPane().add(panel);
        frame.setSize(640, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.show();
        double res1 = dichotomy(left, right, eps1);
        double res2 = dichotomy(left, right, eps2);
        double res3 = dichotomy(left, right, eps3);
        System.out.println(res1 + " " + f(res1));
        System.out.println(res2 + " " + f(res2));
        System.out.println(res3 + " " + f(res3));
        System.out.println();
        double res4 = newton(left, right, eps1);
        double res5 = newton(left, right, eps2);
        double res6 = newton(left, right, eps3);
        System.out.println(res4 + " " + f(res4));
        System.out.println(res5 + " " + f(res5));
        System.out.println(res6 + " " + f(res6));
        System.out.println();
        double res7 = secant(left, right, eps1);
        double res8 = secant(left, right, eps2);
        double res9 = secant(left, right, eps3);
        System.out.println(res7 + " " + f(res7));
        System.out.println(res8 + " " + f(res8));
        System.out.println(res9 + " " + f(res9));
        System.out.println(newtonSpeed(left, right, eps3));
    }
}
