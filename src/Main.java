import TestFunctions.*;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by oddca on 23/03/2018.
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        int randomSeed = 1;
        int dimensions = 1000;
        int swarmSize =  30;
        double p = 0.5;
        int nrRuns = 25;

        int[] S1 = {2, 5, 10, 50, 100};
        int[] S2 = {2, 5, 10, 50, 100, 250};
        int[] S;
        if (dimensions == 100){
            S = S1;
        }
        else {
            S = S2;
        }


        RandomGenerator random = new JDKRandomGenerator(randomSeed);

        Function f1 = new F1(dimensions, random);
        Function f2 = new F2(dimensions, random);
        Function f3 = new F3(dimensions, random);
        Function f4 = new F4(dimensions, random);
        Function f5 = new F5(dimensions, random);
        Function f6 = new F6(dimensions, random);
        Function f7 = new F7(dimensions, random);

        Function[] functions = new Function[]{f7};

        for (int i = 0; i < functions.length; i++) {
            Function function = functions[i];
            double sum = 0.0;
            double[] fitnessValues = new double[nrRuns];
            String fileName = "Results/" + function.getName() + "/" + dimensions + "D.txt";
            PrintWriter writer = new PrintWriter(fileName, "UTF-8");
            for (int j = 0; j < nrRuns; j++) {
                double[] output = CCPSO2.run(function, S, 5000*dimensions, swarmSize, p, random, false);
                double fitness = function.calculate(output) - function.getBias();
                System.out.println(fitness);
                writeResults(fitness, output, writer, ';');
                sum += fitness;
                fitnessValues[j] = fitness;
            }
            writer.close();
            double mean = sum/nrRuns;
            double std = std(fitnessValues, mean);
            System.out.println("Mean: " + mean + " Std: " + std);
        }
    }

    private static double std(double[] values, double mean){
        double sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += Math.pow(values[i] - mean, 2);
        }
        return Math.sqrt((1.0/values.length)*sum);
    }

    private static void writeResults(double result, double[] parameters, PrintWriter writer, char delimiter){
        writer.print(result + "" + delimiter + "[");
        for (int i = 0; i < parameters.length; i++) {
            double value = parameters[i];
            if (i == parameters.length - 1){
                writer.print(value + "]\n");
            }
            else {
                writer.print(value + ",");
            }
        }
    }

}
