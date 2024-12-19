package TestFunctions;

import org.apache.commons.math3.random.RandomGenerator;


/**
 * Created by oddca on 02/04/2018.
 */
public class F3 implements Function {

    private int dimensions;
    private double[] shifted;
    private double bias;

    // Shifted Rosenbrock function
    public F3(int dimensions, RandomGenerator random){
        this.dimensions = dimensions;
        shifted = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            double value = getLowerLimit() + (getUpperLimit() - getLowerLimit()) * random.nextDouble();
            shifted[i] = value;
        }
        bias = 390;
    }

    @Override
    public double calculate(double[] input) {
        double sum = 0;
        for (int i = 0; i < dimensions - 1; i++) {
            sum += 100*Math.pow(Math.pow(input[i] - shifted[i] + 1, 2) - (input[i] - shifted[i] + 1), 2) + Math.pow(input[i] - shifted[i], 2);
        }
        return sum + bias;
    }

    @Override
    public double getUpperLimit() {
        return 100;
    }

    @Override
    public double getLowerLimit() {
        return -100;
    }

    @Override
    public int getDimensions() {
        return dimensions;
    }

    @Override
    public double getBias() {
        return bias;
    }

    @Override
    public String getName() {
        return "F3";
    }
}
