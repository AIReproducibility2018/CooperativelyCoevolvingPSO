package TestFunctions;

import org.apache.commons.math3.random.RandomGenerator;


/**
 * Created by oddca on 02/04/2018.
 */
public class F5 implements Function {

    private int dimensions;
    private double[] shifted;
    private double bias;

    // Shifted Griewank's function
    public F5(int dimensions, RandomGenerator random){
        this.dimensions = dimensions;
        shifted = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            double value = getLowerLimit() + (getUpperLimit() - getLowerLimit()) * random.nextDouble();
            shifted[i] = value;
        }
        bias = -180;
    }

    @Override
    public double calculate(double[] input) {
        double sum = 0.0;
        double product = 1.0;
        for (int i = 0; i < dimensions; i++) {
            sum += Math.pow(input[i] - shifted[i], 2) / 4000;
            product *= Math.cos((input[i] - shifted[i])/Math.sqrt(i+1));
        }
        return sum - product + 1 + bias;
    }

    @Override
    public double getUpperLimit() {
        return 600;
    }

    @Override
    public double getLowerLimit() {
        return -600;
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
        return "F5";
    }
}
