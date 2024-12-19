package TestFunctions;

import org.apache.commons.math3.random.RandomGenerator;


/**
 * Created by oddca on 01/04/2018.
 */
public class F6 implements Function {

    private int dimensions;
    private double[] shifted;
    private double bias;

    // Shifted Ackley's function
    public F6(int dimensions, RandomGenerator random){
        this.dimensions = dimensions;
        shifted = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            double value = getLowerLimit() + (getUpperLimit() - getLowerLimit()) * random.nextDouble();
            shifted[i] = value;
        }
        bias = -140;
    }

    @Override
    public double calculate(double[] input) {
        double sum1 = 0;
        double sum2 = 0;
        for (int i = 0; i < dimensions; i++) {
            sum1 += Math.pow(input[i] - shifted[i], 2);
            sum2 += Math.cos(2*Math.PI*(input[i] - shifted[i]));
        }
        return -20*Math.exp(-0.2*Math.sqrt((1.0/dimensions)*sum1)) - Math.exp((1.0/dimensions)*sum2) + 20 + Math.E + bias;
    }

    @Override
    public double getUpperLimit() {
        return 32;
    }

    @Override
    public double getLowerLimit() {
        return -32;
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
        return "F6";
    }
}
