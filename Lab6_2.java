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

public class Lab6_2 {

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

    public static double[] u0(double[] x) {
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; ++i) {
            result[i] = u0(x[i]);
        }
        return result;
    }

    public static double[] progonka(boolean flag, double[] x, double step,
                                    double a1, double a2, double b1, double b2, double c1, double c2) {
        int amount = x.length;
        double[] result = new double[amount];
        double[] a = new double[amount];
        double[] b = new double[amount];
        double[] c = new double[amount];
        double[] A = new double[amount];
        double[] B = new double[amount];
        double[] func = new double[amount];
        if (flag) {
            a[0] = 0;
            b[0] = a1 - b1 / step;
            c[0] = b1 / step;
            a[amount - 1] = -b2 / step;
            b[amount - 1] = a2 + b2 / step;
            func[0] = c1;
            func[amount - 1] = c2;
        } else {
            if (b1 == 0 & b2 != 0) {
                a[0] = 0;
                b[0] = 1;
                c[0] = 0;
                a[amount - 1] = 2;
                b[amount - 1] = -2 - 2 * step * a2 / b2 + p(x[amount - 1]) * step * step * a2 / b2 - q(x[amount - 1]) * step * step;
                func[0] = c1;
                func[amount - 1] = step * step * p(x[amount - 1]) * c2 / b2 - 2 * step * c2 / b2 - f(x[amount - 1]) * step * step;
            } else if (b2 == 0 & b1 != 0) {
                a[0] = 0;
                b[0] = -2 + 2 * step * a1 / b1 - a1 * step * step * p(x[0]) / b1 + q(x[0]) * step * step;
                c[0] = 2;
                a[amount - 1] = 0;
                b[amount - 1] = 1;
                func[0] = f(x[0]) * step * step - c1 * step * step * p(x[0]) / b1 + 2 * step * c1 / b1;
                func[amount - 1] = c2;
            } else if (b1 == 0 & b2 == 0) {
                a[0] = 0;
                b[0] = 1;
                c[0] = 0;
                a[amount - 1] = 0;
                b[amount - 1] = 1;
                func[0] = c1;
                func[amount - 1] = c2;
            } else {
                a[0] = 0;
                b[0] = -2 + 2 * step * a1 / b1 - a1 * step * step * p(x[0]) / b1 + q(x[0]) * step * step;
                c[0] = 2;
                a[amount - 1] = 2;
                b[amount - 1] = -2 - 2 * step * a2 / b2 - p(x[amount - 1]) * step * step * a2 / b2 + q(x[amount - 1]) * step * step;
                func[0] = f(x[0]) * step * step - c1 * step * step * p(x[0]) / b1 + 2 * step * c1 / b1;
                func[amount - 1] = -step * step * p(x[amount - 1]) * c2 / b2 - 2 * step * c2 / b2 + f(x[amount - 1]) * step * step;
            }
        }
        for (int i = 1; i < amount - 1; ++i) {
            func[i] = f(x[i]);
        }
        for (int i = 1; i < amount - 1; ++i) {
            a[i] = 1 / step / step - p(x[i]) / step / 2;
            b[i] = -2 / step / step + q(x[i]);
            c[i] = 1 / step / step + p(x[i]) / step / 2;
        }
        A[0] = -c[0] / b[0];
        B[0] = func[0] / b[0];
        for (int i = 1; i < amount; i++) {
            A[i] = -c[i] / (b[i] + a[i] * A[i - 1]);
            B[i] = (func[i] - a[i] * B[i - 1]) / (b[i] + a[i] * A[i - 1]);
        }
        A[amount - 1] = 0;
        B[amount - 1] = (func[amount - 1] - a[amount - 1] * B[amount - 2]) / (b[amount - 1] + a[amount - 1] * A[amount - 2]);
        result[amount - 1] = B[amount - 1];
        for (int i = 1; i < amount; ++i) {
            result[amount - i - 1] = B[amount - i - 1] + A[amount - i - 1] * result[amount - i];
        }
        return result;
    }

    // u''+tg(x)*u' - 2x*u/cosx = 2 - 2x^3/cosx
    // 2u(0)-u'(0)=-1;
    // 3u(1)+u'(1)=8.0647
    // u0(x) = sinx+x^2

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
        double[] x = new double[(int) Math.round((right - left) / h) + 1];
        for (int i = 0; i < x.length; ++i) {
            x[i] = left + i * h;
        }
        double[] y1 = progonka(true, x, h, alpha1, alpha2, beta1, beta2, gamma1, gamma2);
        double[] y2 = progonka(false, x, h, alpha1, alpha2, beta1, beta2, gamma1, gamma2);
        double[] y0 = u0(x);
        XYSeriesCollection data = new XYSeriesCollection();
        XYSeries graphY1 = new XYSeries("Y1");
        XYSeries graphY2 = new XYSeries("Y2");
        XYSeries graphY0 = new XYSeries("Y0");
        for (int i = 1; i < x.length - 1; i++) {
            graphY1.add(left + i * h, y1[i]);
            graphY2.add(left + i * h, y2[i]);
            graphY0.add(left + i * h, u0(left + i * h));
        }
        data.addSeries(graphY0);
        data.addSeries(graphY1);
        data.addSeries(graphY2);
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
        int amount = 20;
        double[] steps = new double[amount];
        double[] max1 = new double[amount];
        double[] max2 = new double[amount];
        for (int i = 0; i < amount; ++i) {
            steps[i] = right / (10 + 10 * i);
            max1[0] = 0;
            max2[0] = 0;
            double[] x1 = new double[(int) Math.round((right - left) / steps[i]) + 1];
            for (int j = 0; j < x1.length; ++j) {
                x1[j] = left + j * steps[i];
            }
            double[] u1 = progonka(true, x1, steps[i], alpha1, alpha2, beta1, beta2, gamma1, gamma2);
            double[] u2 = progonka(false, x1, steps[i], alpha1, alpha2, beta1, beta2, gamma1, gamma2);
            double[] u0 = u0(x1);
            for (int j = 1; j < u1.length - 1; ++j) {
                double r1 = Math.abs(u0[j] - u1[j]);
                double r2 = Math.abs(u0[j] - u2[j]);
                if (r1 > max1[i]) {
                    max1[i] = r1;
                }
                if (r2 > max2[i]) {
                    max2[i] = r2;
                }
            }
        }
        XYSeriesCollection data2 = new XYSeriesCollection();
        XYSeries graphU1 = new XYSeries("U1");
        XYSeries graphU2 = new XYSeries("U2");
        for (int i = 1; i < max1.length - 5; ++i) {
//            graphU1.add(Math.log(steps[i])+ 5.3, Math.log(max1[i])+6.5);
//            graphU2.add(Math.log(steps[i])+5.3, Math.log(max2[i])+13);
            graphU1.add(Math.log(steps[i]), Math.log(max1[i]));
            graphU2.add(Math.log(steps[i]), Math.log(max2[i]));
//            System.out.println(max1[i] + " " + max2[i]);
        }
        data2.addSeries(graphU1);
        data2.addSeries(graphU2);
        XYDataset dataset2 = data2;
        JFreeChart chart2 = ChartFactory.createXYLineChart("Graph2", "Ln(step)", "Ln(err)", dataset2);
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
        JFrame frame2 = new JFrame("Err graph");
        ChartPanel panel2 = new ChartPanel(chart2);
        frame2.getContentPane().add(panel2);
        frame2.setSize(640, 640);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.show();
    }
}
