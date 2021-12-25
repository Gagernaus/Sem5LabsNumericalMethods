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

public class Lab3 {

    public static double f(double x) {
        return Math.tanh(x);
    }

    public static double rectangle(int k, double[] knots) {
        return (knots[k + 1] - knots[k]) * f(knots[k]);
    }

    public static double intRectangle(double[] knots) {
        double result = 0;
        for (int i = 0; i < knots.length - 1; ++i) {
            result += rectangle(i, knots);
        }
        return result;
    }

    public static double trapeze(int k, double[] knots) {
        return (f(knots[k]) + f(knots[k + 1])) * (knots[k + 1] - knots[k]) / 2;
    }

    public static double intTrapeze(double[] knots) {
        double result = 0;
        for (int i = 0; i < knots.length - 1; ++i) {
            result += trapeze(i, knots);
        }
        return result;
    }

    public static double simpson(int k, double[] knots, double step) {
        return step * (f(knots[k]) + 4 * f(knots[k] + step / 2) + f(knots[k] + step)) / 6;
    }


    public static double intSimpson(double[] knots, double step) {
        double result = 0;
        for (int i = 0; i < knots.length - 1; i++) {
            result += simpson(i, knots, step);
        }
        return result;
    }


//    public static double intSimpson(double[] knots, double step) {
//        double result = 0;
//        for (int i = 0; i < knots.length - 1; ++i) {
//            result += simpson(i, knots, step);
//        }
//        return result;
//    }

    public static double[] makeKnots1(double step, double left, double right) {
        double[] result = new double[(int) Math.round((right - left) / step) + 1];
        result[0] = left;
        result[result.length - 1] = right;
        for (int i = 1; i < result.length - 1; ++i) {
            result[i] = left + i * step;
        }
        return result;
    }

    public static double[] makeKnots2(double step, double left, double right) {
        double[] result = new double[(int) Math.round((right - left) / step)];
        for (int i = 0; i < result.length; ++i) {
            result[i] = left + i * step;
        }
        return result;
    }

    public static double correct(double a, double b) {
        return Math.log(Math.cosh(b)) - Math.log(Math.cosh(a));
    }

    public static void main(String[] args) {
        double left = 0;
        double right = 5;
        double step = 0.1;
        System.out.println("Step = " + step);
        double[] knots = makeKnots1(step, left, right);
//        knots[0] = left;
//        knots[knots.length - 1] = right;
//        for (int i = 1; i < knots.length - 1; ++i) {
//            knots[i] = left + i * step;
//        }
//        for (int i = 0; i < knots.length; ++i) {
//            System.out.println(knots[i]);
//        }
        double result = Math.log(Math.cosh(right)) - Math.log(Math.cosh(left));
        System.out.println("Accurate integral result = " + result);
        double rectangleResult = intRectangle(knots);
        System.out.println("Rectangle method result = " + rectangleResult + " with error = " + Math.abs(result - rectangleResult));
        double trapezeResult = intTrapeze(knots);
        System.out.println("Trapeze method result = " + trapezeResult + " with error = " + Math.abs(result - trapezeResult));
        double simpsonResult = intSimpson(knots, step);
        System.out.println("Simpson method result = " + simpsonResult + " with error = " + Math.abs(result - simpsonResult));
        double currentStep = 0.1;
        double deltaStep = 0.00001;
        double[] stepLn = new double[(int) Math.round((currentStep - deltaStep) / deltaStep)];
        double[] errorLnR = new double[stepLn.length];
        double[] errorLnT = new double[stepLn.length];
        double[] errorLnS = new double[stepLn.length];
        XYSeriesCollection data = new XYSeriesCollection();
        XYSeries rectangle = new XYSeries("Rectangle");
        XYSeries trapeze = new XYSeries("Trapeze");
        XYSeries simpson = new XYSeries("Simpson");
        for (int i = 0; i < stepLn.length; ++i) {
//            stepLn[i] = Math.log(currentStep);
            stepLn[i] = currentStep;
//            double[] knots1 = makeKnots(currentStep, left, right);
            double[] knots2 = makeKnots2(currentStep, left, right);
//            for (int j = 0; j < knots1.length; ++j) {
//                System.out.println(knots1[j]);
//            }
//            double rect = intRectangle(knots1);
//            double trap = intTrapeze(knots1);
            double errorSimp = 0;
            double errRect = 0;
            double errTrap = 0;
            for (int j = 0; j < knots2.length - 1; ++j) {
                double rect = rectangle(j, knots2);
                double trap = trapeze(j, knots2);
                double simp = simpson(j, knots2, currentStep);
                double correct = correct(knots2[j], knots2[j + 1]);
                errRect += Math.abs(correct - rect);
                errTrap += Math.abs(correct - trap);
                errorSimp += Math.abs(correct - simp);
            }
//            errorLnR[i] = Math.log(Math.abs(result - rect));
//            errorLnT[i] = Math.log(Math.abs(result - trap));
//            errorLnR[i] = Math.log(errRect);
//            errorLnT[i] = Math.log(errTrap);
//            errorLnS[i] = Math.log(errorSimp);
            errorLnR[i] = errRect;
            errorLnT[i] = errTrap;
            errorLnS[i] = errorSimp;
//            errorLnS[i] = 0;
//            errorLnR[i] = (Math.abs(result - rect));
//            errorLnT[i] = (Math.abs(result - trap));
//            errorLnS[i] = (Math.abs(result - simp));
            currentStep -= deltaStep;
            rectangle.add(stepLn[i], errorLnR[i]);
            trapeze.add(stepLn[i], errorLnT[i]);
//            if (((int) Math.round((right - left) / (currentStep))) % 2 == 1) {
//                simpson.add(stepLn[i], errorLnS[i]);
//            }
            simpson.add(stepLn[i], errorLnS[i]);
        }
//        currentStep = 0.1;
//        double[] simpStepLn = new double[stepLn.length];
//        for (int i = 0; i < stepLn.length; ++i) {
//            simpStepLn[i] = 0;
//        }
//        int index = 0;
//        while (currentStep > 0) {
//            if ((int) Math.round((right - left) / currentStep) % 2 == 1) {
//                simpStepLn[index] = Math.log(currentStep);
//                double[] knots1 = makeKnots(currentStep, left, right);
//                double simp = intSimpson(knots1, currentStep);
//                errorLnS[index] = Math.log(Math.abs(result - simp));
//                simpson.add(simpStepLn[index], errorLnS[index]);
//                ++index;
//            }
//            currentStep -= deltaStep;
//        }
        data.addSeries(rectangle);
        data.addSeries(trapeze);
        data.addSeries(simpson);
        XYDataset dataset = data;
        JFreeChart chart = ChartFactory.createXYLineChart("Chart", "ln(step)", "ln(err)", dataset);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesPaint(2, Color.ORANGE);
        renderer.setSeriesStroke(0, new BasicStroke(1.0f));
        renderer.setSeriesStroke(1, new BasicStroke(1.0f));
        renderer.setSeriesStroke(2, new BasicStroke(1.0f));
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
