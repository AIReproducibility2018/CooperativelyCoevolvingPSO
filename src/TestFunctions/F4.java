package TestFunctions;

import org.apache.commons.math3.random.RandomGenerator;


/**
 * Created by oddca on 29/03/2018.
 */
public class F4 implements Function {

    private int dimensions;
    private double[] shifted;
    private double bias;

    // Shifted Rastrigin's function
    public F4(int dimensions, RandomGenerator random){
        this.dimensions = dimensions;
        shifted = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            double value = getLowerLimit() + (getUpperLimit() - getLowerLimit()) * random.nextDouble();
            shifted[i] = value;
        }
        bias = -330;
    }

    @Override
    public double calculate(double[] input) {
        double sum = 0;
        for (int i = 0; i < dimensions; i++) {
            sum += Math.pow(input[i] - shifted[i], 2) - 10*Math.cos(2*Math.PI*(input[i] - shifted[i])) + 10;
        }
        return sum + bias;
    }

    @Override
    public double getUpperLimit() {
        return 5;
    }

    @Override
    public double getLowerLimit() {
        return -5;
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
        return "F4";
    }
}
