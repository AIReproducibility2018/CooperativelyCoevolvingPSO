package TestFunctions;

import org.apache.commons.math3.random.RandomGenerator;

/**
 * Created by oddca on 23/03/2018.
 */
public class F1 implements Function {

    private int dimensions;
    private double[] shifted;
    private double bias;

    // Shifted Sphere function
    public F1(int dimensions, RandomGenerator random){
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
        double sum = 0;
        for (int i = 0; i < dimensions; i++) {
            sum += Math.pow(input[i] - shifted[i], 2);
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
    public double getBias(){
        return bias;
    }

    @Override
    public String getName() {
        return "F1";
    }
}
