package TestFunctions;

import org.apache.commons.math3.random.RandomGenerator;

/**
 * Created by oddca on 02/04/2018.
 */
public class F2 implements Function {

    private int dimensions;
    private double[] shifted;
    private double bias;

    // Schwefel function
    public F2(int dimensions, RandomGenerator random){
        this.dimensions = dimensions;
        shifted = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            double value = getLowerLimit() + (getUpperLimit() - getLowerLimit()) * random.nextDouble();
            shifted[i] = value;
        }
        bias = -450;
    }

    @Override
    public double calculate(double[] input) {
        double maxValue = 0;
        for (int i = 0; i < dimensions; i++) {
            if (Math.abs(input[i] - shifted[i]) > maxValue){
                maxValue = Math.abs(input[i] - shifted[i]);
            }
        }
        return maxValue + bias;
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
        return "F2";
    }
}
