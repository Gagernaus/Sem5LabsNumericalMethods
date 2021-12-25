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

public class Lab6 {

    public static double f(double x) {
        return 2 - 2 * Math.pow(x, 3) / Math.cos(x);
    }

    public static double p(double x) {
        return Math.tan(x);
    }

    public static double q(double x) {
        return -2 * x / Math.cos(x);
    }

    public static double u0(double x) {
        return Math.sin(x) + x * x;
    }

//    public static double secondDerivative(double x, double u, double ufd) {
//        return -p(x) * ufd - q(x) * u + f(x);
//    }

    // u''+tg(x)*u' - 2x*u/cosx = 2 - 2x^3/cosx
    // 2u(0)-u'(0)=-1;
    // 3u(1)+u'(1)=8.0647
    // u0(x) = sinx+x^2

//    public static double[] progonka(boolean flag, double left, double right, double step, double a1, double a2, double b1, double b2, double c1, double c2) {
//        int amount = (int) Math.round((right - left) / step) + 1;
//        double[] pk = new double[amount];
//        double[] qk = new double[amount];
//        double[] fk = new double[amount];
//        for (int i = 0; i < amount; ++i) {
//            double x = left + i * step;
//            pk[i] = p(x);
//            qk[i] = q(x);
//            fk[i] = f(x);
//        }
//        double[] ak = new double[amount];
//        double[] bk = new double[amount];
//        double[] ck = new double[amount];
//        double[] dk = new double[amount];
//        if (flag) {
//            ak[0] = 0;
//            bk[0] = a1 - b1 / step;
//            ck[0] = b1 / step;
//            dk[0] = c1;
//        } else {
//            ak[0] = 0;
//            bk[0] = -2 + 2 * step * a1 / b1 - a1 * step * step * pk[0] / b1 + qk[0] * step * step;
//            ck[0] = 2;
//            dk[0] = fk[0] * step * step - c1 * step * step * pk[0] / b1 + 2 * step * c1 / b1;
//        }
//        for (int i = 1; i < amount - 1; ++i) {
//            ak[i] = 1 - step * pk[i] / 2;
//            bk[i] = -2 + step * step * qk[i];
//            ck[i] = 1 + step * pk[i] / 2;
//            dk[i] = fk[i] * step * step;
//        }
//        if (flag) {
//            ak[amount - 1] = -b2 / step;
//            bk[amount - 1] = a2 + b2 / step;
//            ck[amount - 1] = 0;
//            dk[amount - 1] = c2;
//        } else {
//            ak[amount - 1] = 2;
//            bk[amount - 1] = -2 - 2 * step * a2 / b2 - pk[amount - 1] * step * step * a2 / b2 + qk[amount - 1] * step * step;
//            ck[amount - 1] = 0;
//            dk[amount - 1] = -step * step * pk[amount - 1] * c2 / b2 - 2 * step * c2 / b2 + fk[amount - 1] * step * step;
//        }
//        double[] ak1 = new double[amount];
//        double[] bk1 = new double[amount];
//        ak1[0] = -ck[0] / bk[0];
//        bk1[0] = dk[0] / bk[0];
//        for (int i = 1; i < amount; ++i) {
//            ak1[i] = -ck[i] / (bk[i] + ak[i] * ak1[i - 1]);
//            bk1[i] = (dk[i] - ak[i] * bk1[i - 1]) / (bk[i] + ak[i] * ak1[i - 1]);
//        }
//        double[] result = new double[amount];
//        result[amount - 1] = bk1[amount - 1];
//        for (int i = amount - 2; i > -1; i--) {
//            result[i] = bk1[i] + ak1[i] * result[i + 1];
//        }
//        return result;
//    }

    public static double[] progonka(boolean flag, double left, double right, double step, double a1, double a2, double b1, double b2, double c1, double c2) {
        int amount = (int) Math.round((right - left) / step) + 1;
        double[] pk = new double[amount];
        double[] qk = new double[amount];
        double[] fk = new double[amount];
        for (int i = 0; i < amount; ++i) {
            double x = left + i * step;
            pk[i] = p(x);
            qk[i] = q(x);
            fk[i] = f(x);
        }
        double[] ak = new double[amount];
        double[] bk = new double[amount];
        double[] ck = new double[amount];
        double[] dk = new double[amount];
        if (flag) {
            ak[0] = 0;
            bk[0] = a1 - b1 / step;
            ck[0] = b1 / step;
            dk[0] = c1;
        } else {
            ak[0] = 0;
            bk[0] = -2 + 2 * step * a1 / b1 - a1 * step * step * pk[0] / b1 + qk[0] * step * step;
            ck[0] = 2;
            dk[0] = fk[0] * step * step - c1 * step * step * pk[0] / b1 + 2 * step * c1 / b1;
        }
        for (int i = 1; i < amount - 1; ++i) {
            ak[i] = 1 / step / step - pk[i] / step / 2;
            bk[i] = -2 / step / step + qk[i];
            ck[i] = 1 / step / step + pk[i] / step / 2;
            dk[i] = fk[i];
        }
        if (flag) {
            ak[amount - 1] = -b2 / step;
            bk[amount - 1] = a2 + b2 / step;
//            ck[amount - 1] = 0;
            dk[amount - 1] = c2;
        } else {
            ak[amount - 1] = 2;
            bk[amount - 1] = -2 - 2 * step * a2 / b2 - pk[amount - 1] * step * step * a2 / b2 + qk[amount - 1] * step * step;
//            ck[amount - 1] = 0;
            dk[amount - 1] = -step * step * pk[amount - 1] * c2 / b2 - 2 * step * c2 / b2 + fk[amount - 1] * step * step;
        }
        double[] ak1 = new double[amount];
        double[] bk1 = new double[amount];
        ak1[0] = -ck[0] / bk[0];
        bk1[0] = dk[0] / bk[0];
        for (int i = 1; i < amount; ++i) {
            ak1[i] = -ck[i] / (bk[i] + ak[i] * ak1[i - 1]);
            bk1[i] = (dk[i] - ak[i] * bk1[i - 1]) / (bk[i] + ak[i] * ak1[i - 1]);
        }
        double[] result = new double[amount];
        result[amount - 1] = bk1[amount - 1];
        for (int i = amount - 2; i > -1; i--) {
            result[i] = bk1[i] + ak1[i] * result[i + 1];
        }
        return result;
    }

    public static void main(String[] args) {
        double left = 0;
        double right = 1;
        double h = 0.05;
        double alpha1 = 2;
        double beta1 = -1;
        double gamma1 = -1;
        double alpha2 = 3;
        double beta2 = 1;
        double gamma2 = 8.0647;
        int amount = (int) Math.round((right - left) / h) + 1;
        double[] y1 = progonka(true, left, right, h, alpha1, alpha2, beta1, beta2, gamma1, gamma2);
        double[] y2 = progonka(false, left, right, h, alpha1, alpha2, beta1, beta2, gamma1, gamma2);
        XYSeriesCollection data = new XYSeriesCollection();
        XYSeries graphU1 = new XYSeries("U1");
        XYSeries graphU2 = new XYSeries("U2");
        XYSeries graphU0 = new XYSeries("U0");
        for (int i = 1; i < amount - 1; i++) {
            graphU1.add(left + i * h, y1[i]);
            graphU2.add(left + i * h, y2[i]);
            graphU0.add(left + i * h, u0(left + i * h));
        }
        data.addSeries(graphU0);
        data.addSeries(graphU1);
        data.addSeries(graphU2);
        XYDataset dataset = data;
        JFreeChart chart = ChartFactory.createXYLineChart("Graph", "X", "F(X)", dataset);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesPaint(2, Color.ORANGE);
        renderer.setSeriesStroke(0, new BasicStroke(0.5f));
        renderer.setSeriesStroke(1, new BasicStroke(0.5f));
        renderer.setSeriesStroke(2, new BasicStroke(0.5f));
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

        XYSeriesCollection data2 = new XYSeriesCollection();
        XYSeries err1 = new XYSeries("err1");
        XYSeries err2 = new XYSeries("err2");

//        double currentStep = 0.1;
//        double deltaStep = 0.001;
//        while (currentStep >= deltaStep) {
//            double[] u1 = progonka(true, left, right, currentStep, alpha1, alpha2, beta1, beta2, gamma1, gamma2);
//            double[] u2 = progonka(false, left, right, currentStep, alpha1, alpha2, beta1, beta2, gamma1, gamma2);
//            double max1 = 0;
//            double max2 = 0;
//            int am = (int) Math.round((right - left) / currentStep) + 1;
//            for (int i = 1; i < am; ++i) {
//                double r1 = Math.abs(u0(left + i * currentStep) - u1[i]);
//                double r2 = Math.abs(u0(left + i * currentStep) - u2[i]);
//                if (r1 > max1) max1 = r1;
//                if (r2 > max2) max2 = r2;
//            }
//            err1.add(Math.log(currentStep), Math.log(max1));
//            err2.add(Math.log(currentStep), Math.log(max2));
//            currentStep -= deltaStep;
//        }
        int am = 20;
        double[] steps = new double[am];
        double[] max1 = new double[am];
        double[] max2 = new double[am];
        for (int i = 0; i < am; ++i) {
            steps[i] = 1.0 / (10 + i * 10);
            max1[i] = 0;
            max2[i] = 0;
            double[] u1 = progonka(true, left, right, steps[i], alpha1, alpha2, beta1, beta2, gamma1, gamma2);
            double[] u2 = progonka(false, left, right, steps[i], alpha1, alpha2, beta1, beta2, gamma1, gamma2);
            double[] u0 = new double[u1.length];
            for (int j = 0; j < u1.length; ++j) {
                u0[i] = u0(left + steps[i] * j);
            }
            for (int j = 0; j < u1.length; ++j) {
                double r1 = Math.abs(u0[j] - u1[j]);
                double r2 = Math.abs(u0[j] - u2[j]);
                System.out.println(r1 + " " + r2);
                if (r1 > max1[i]) max1[i] = r1;
                if (r2 > max2[i]) max2[i] = r2;
            }
            System.out.println();
            err1.add(Math.log(steps[i]) + 7.7, Math.log(max1[i]) - 0.61);
            err2.add(Math.log(steps[i]) + 7.7, Math.log(max2[i]) - 0.61);
        }
        data2.addSeries(err1);
        data2.addSeries(err2);
        XYDataset dataset2 = data2;
        JFreeChart chart2 = ChartFactory.createXYLineChart("Graph2", "Ln(step)", "Ln(MaxErr)", dataset2);
        XYPlot plot2 = chart2.getXYPlot();
        XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
        renderer2.setSeriesPaint(0, Color.RED);
        renderer2.setSeriesPaint(1, Color.BLUE);
        renderer2.setSeriesStroke(0, new BasicStroke(0.5f));
        renderer2.setSeriesStroke(1, new BasicStroke(0.5f));
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
    }
}
