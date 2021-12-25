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

public class Lab5 {

    public static double u0(double x) {
        return Math.sin(x) + x * x;
    }

    public static double u0der(double x) {
        return Math.cos(x) + 2 * x;
    }

    public static double p(double x) {
        return Math.tan(x);
    }

    public static double q(double x) {
        return -2 * x / Math.cos(x);
    }

    public static double f(double x) {
        return 2 - 2 * Math.pow(x, 3) / Math.cos(x);
    }

    public static double secondDerivative(double x, double u, double ufd) {
        return -p(x) * ufd - q(x) * u + f(x);
    }

    public static double euler(int amount, double step, double start, double u, double ufd) {
        double x = start;
        double y = u;
        double firstDerivative = ufd;
        for (int i = 0; i < amount; ++i) {
            firstDerivative += step * secondDerivative(x, y, firstDerivative);
            y += step * firstDerivative;
            x += step;
        }
        return y;
    }

    public static double rungeKutta(int amount, double step, double start, double u, double ufd) {
        double x = start;
        double y = u;
        double firstDerivative = ufd;
        for (int i = 0; i < amount; ++i) {
            double q1 = secondDerivative(x, y, firstDerivative);
            double k1 = firstDerivative;
            double q2 = secondDerivative(x + step / 2, y + step * k1 / 2, firstDerivative + step * q1 / 2);
            double k2 = firstDerivative + step * q1 / 2;
            double q3 = secondDerivative(x + step / 2, y + step * k2 / 2, firstDerivative + step * q2 / 2);
            double k3 = firstDerivative + step * q2 / 2;
            double q4 = secondDerivative(x + step, y + step * k3, firstDerivative + step * q3);
            double k4 = firstDerivative + step * q3;
            firstDerivative += step * (q1 + 2 * q2 + 2 * q3 + q4) / 6;
            y += step * (k1 + 2 * k2 + 2 * k3 + k4) / 6;
            x += step;
        }
        return y;
    }

    public static double adams(int amount, double step, double start, double u, double ufd) {
        if (amount == 0) {
            return u;
        } else if (amount == 1) {
            return rungeKutta(1, step, start, u, ufd);
        } else if (amount == 2) return rungeKutta(2, step, start, u, ufd);
        double x = start;
        double y1 = u;
        double fd1 = ufd;
        double y2 = u0(start + step);
        double fd2 = u0der(start + step);
        double y3 = u0(start + 2 * step);
        double fd3 = u0der(start + 2 * step);
        for (int i = 0; i < amount - 2; ++i) {
            double q3 = 5 * secondDerivative(x, y1, fd1);
            double k3 = 5 * fd1;
            double q2 = -16 * secondDerivative(x + step, y2, fd2);
            double k2 = -16 * fd2;
            double q1 = 23 * secondDerivative(x + 2 * step, y3, fd3);
            double k1 = 23 * fd3;
            fd1 = fd2;
            fd2 = fd3;
            fd3 += step * (q1 + q2 + q3) / 12;
            y1 = y2;
            y2 = y3;
            y3 += step * (k1 + k2 + k3) / 12;
            x += step;
        }
        return y3;
    }

    public static double correctionRK(int amount, double step, double start, double u, double ufd) {
        double y1 = rungeKutta(amount, step, start, u, ufd);
        double y2 = rungeKutta(amount * 2, step / 2, start, u, ufd);
        return (y2 - y1) / (Math.pow(2, 4) - 1);
    }

    public static double correctionE(int amount, double step, double start, double u, double ufd) {
        double y1 = euler(amount, step, start, u, ufd);
        double y2 = euler(amount * 2, step / 2, start, u, ufd);
        return (y2 - y1) / (Math.pow(2, 1) - 1);
    }

    public static double correctionA(int amount, double step, double start, double u, double ufd) {
        double y1 = adams(amount, step, start, u, ufd);
        double y2 = adams(amount * 2, step / 2, start, u, ufd);
        return (y2 - y1) / (Math.pow(2, 3) - 1);
    }


    // u''+tg(x)*u'-2x*u/cos(x)=2-2x^3/cosx
    // u(0)=0 u'(0)=1 u0(x)=sin(x)+x^2
    public static void main(String[] args) {
        double left = 0;
        double right = 1;
        double h = 0.05;
        int amount = (int) Math.round((right - left) / h);
        double u0 = 0;
        double u0der = 1;
        System.out.println("Euler " + euler(amount, h, left, u0, u0der));
        System.out.println("Runge-Kutta " + rungeKutta(amount, h, left, u0, u0der));
        System.out.println("Adams " + adams(amount, h, left, u0, u0der));
        System.out.println("Correct " + u0(right));
        System.out.println("Runge correction for Runge-Kutta " + correctionRK(amount, h, left, u0, u0der));
        System.out.println("Runge correction for Euler " + correctionE(amount, h, left, u0, u0der));
        System.out.println("Runge correction for Adams " + correctionA(amount, h, left, u0, u0der));
        XYSeriesCollection data = new XYSeriesCollection();
        XYSeries function = new XYSeries("function");
        XYSeries euler = new XYSeries("Euler");
        XYSeries rungeKutta = new XYSeries("Runge-Kutta");
        XYSeries adams = new XYSeries("Adams");
        for (int i = 0; i < amount; ++i) {
            function.add(left + h * i, u0(left + h * i));
            euler.add(left + h * i, euler(i, h, left, u0, u0der));
            rungeKutta.add(left + h * i, rungeKutta(i, h, left, u0, u0der));
            adams.add(left + h * i, adams(i, h, left, u0, u0der));
        }
        XYSeriesCollection data3 = new XYSeriesCollection();
        XYSeries eulerLn = new XYSeries("EulerLn");
        XYSeries rungeKuttaLn = new XYSeries("Runge-KuttaLn");
        XYSeries adamsLn = new XYSeries("AdamsLn");
        double currentStep = 0.1;
        double deltaStep = 0.0035;
        while (currentStep >= deltaStep) {
            int am = (int) Math.round((right - left) / currentStep);
            double maxE = 0;
            double maxR = 0;
            double maxA = 0;
            for (int i = 0; i < am; ++i) {
                double eul = euler(i, currentStep, left, u0, u0der);
                double eul2 = euler(i * 2, currentStep / 2, left, u0, u0der);
                double eulc = (eul2 - eul) / (Math.pow(2, 1) - 1);
                double a = Math.abs(u0(left + i * currentStep) - eul2 - eulc);
                if (a > maxE) maxE = a;
                double rung = rungeKutta(i, currentStep, left, u0, u0der);
                double rung2 = rungeKutta(i * 2, currentStep / 2, left, u0, u0der);
                double rungc = (rung2 - rung) / (Math.pow(2, 4) - 1);
                a = Math.abs(u0(left + i * currentStep) - rung2 - rungc);
                if (a > maxR) maxR = a;
                double ada = adams(i, currentStep, left, u0, u0der);
                double ada2 = adams(i * 2, currentStep / 2, left, u0, u0der);
                double adac = (ada2 - ada) / (Math.pow(2, 3) - 1);
                a = Math.abs(u0(left + i * currentStep) - ada2 - adac);
                if (a > maxA) maxA = a;
            }
            eulerLn.add(Math.log(currentStep), Math.log(maxE));
            rungeKuttaLn.add(Math.log(currentStep), Math.log(maxR));
            adamsLn.add(Math.log(currentStep), Math.log(maxA));
            currentStep -= deltaStep;
        }
        data3.addSeries(eulerLn);
        data3.addSeries(rungeKuttaLn);
        data3.addSeries(adamsLn);
        XYDataset dataset3 = data3;
        XYSeries eulerC = new XYSeries("EulerC");
        XYSeries rungeKuttaC = new XYSeries("Runge-KuttaC");
        XYSeries adamsC = new XYSeries("AdamsC");
        for (int i = 0; i < amount; i++) {
            double eul = euler(i, h, left, u0, u0der);
            double eul2 = euler(i * 2, h / 2, left, u0, u0der);
            double eulc = (eul2 - eul) / (Math.pow(2, 1) - 1);
//            if (i % 2 == 0)
            eulerC.add(left + h * i, eul2 + eulc);
            double rung = rungeKutta(i, h, left, u0, u0der);
            double rung2 = rungeKutta(i * 2, h / 2, left, u0, u0der);
            double rungc = (rung2 - rung) / (Math.pow(2, 4) - 1);
//            if (i % 2 == 0)
            rungeKuttaC.add(left + h * i, rung2 + rungc);
            double ada = adams(i, h, left, u0, u0der);
            double ada2 = adams(i * 2, h / 2, left, u0, u0der);
            double adac = (ada2 - ada) / (Math.pow(2, 3) - 1);
//            if (i % 2 == 0)
            adamsC.add(left + h * i, ada2 + adac);
        }
        XYSeriesCollection data2 = new XYSeriesCollection();
        data2.addSeries(function);
        data2.addSeries(eulerC);
        data2.addSeries(rungeKuttaC);
        data2.addSeries(adamsC);
        XYDataset dataset2 = data2;

        data.addSeries(function);
        data.addSeries(euler);
        data.addSeries(rungeKutta);
        data.addSeries(adams);
        XYDataset dataset = data;
        JFreeChart chart = ChartFactory.createXYLineChart("Graph", "X", "F(X)", dataset);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesPaint(2, Color.ORANGE);
        renderer.setSeriesPaint(3, Color.PINK);
        renderer.setSeriesStroke(0, new BasicStroke(0.5f));
        renderer.setSeriesStroke(1, new BasicStroke(0.5f));
        renderer.setSeriesStroke(2, new BasicStroke(0.5f));
        renderer.setSeriesStroke(3, new BasicStroke(0.5f));
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

        JFreeChart chart2 = ChartFactory.createXYLineChart("Graph2", "X", "F(X)", dataset2);
        XYPlot plot2 = chart2.getXYPlot();
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        renderer2.setSeriesPaint(0, Color.RED);
        renderer2.setSeriesPaint(1, Color.BLUE);
        renderer2.setSeriesPaint(2, Color.ORANGE);
        renderer2.setSeriesPaint(3, Color.GREEN);
        renderer2.setSeriesStroke(0, new BasicStroke(0.5f));
        renderer2.setSeriesStroke(1, new BasicStroke(0.5f));
        renderer2.setSeriesStroke(2, new BasicStroke(0.5f));
        renderer2.setSeriesStroke(3, new BasicStroke(0.5f));
        plot2.setOutlinePaint(Color.BLACK);
        plot2.setOutlineStroke(new BasicStroke(2.0f));
        plot2.setRenderer(renderer2);
        plot2.setRangeGridlinesVisible(true);
        plot2.setRangeGridlinePaint(Color.MAGENTA);
        plot2.setDomainGridlinesVisible(true);
        plot2.setDomainGridlinePaint(Color.MAGENTA);
        JFrame frame2 = new JFrame("Function graph");
        ChartPanel panel2 = new ChartPanel(chart2);
        frame2.getContentPane().add(panel2);
        frame2.setSize(640, 640);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.show();

        JFreeChart chart3 = ChartFactory.createXYLineChart("Graph3", "Ln(step)", "Ln(MaxErr)", dataset3);
        XYPlot plot3 = chart3.getXYPlot();
        XYLineAndShapeRenderer renderer3 = new XYLineAndShapeRenderer();
        renderer3.setSeriesPaint(0, Color.RED);
        renderer3.setSeriesPaint(1, Color.BLUE);
        renderer3.setSeriesPaint(2, Color.ORANGE);
        renderer3.setSeriesStroke(0, new BasicStroke(0.5f));
        renderer3.setSeriesStroke(1, new BasicStroke(0.5f));
        renderer3.setSeriesStroke(2, new BasicStroke(0.5f));
        plot3.setOutlinePaint(Color.BLACK);
        plot3.setOutlineStroke(new BasicStroke(2.0f));
        plot3.setRenderer(renderer3);
        plot3.setRangeGridlinesVisible(true);
        plot3.setRangeGridlinePaint(Color.MAGENTA);
        plot3.setDomainGridlinesVisible(true);
        plot3.setDomainGridlinePaint(Color.MAGENTA);
        JFrame frame3 = new JFrame("Function graph");
        ChartPanel panel3 = new ChartPanel(chart3);
        frame3.getContentPane().add(panel3);
        frame3.setSize(640, 640);
        frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame3.show();
    }
}
