package TestFunctions;

import org.apache.commons.math3.random.RandomGenerator;

import java.util.Random;

/**
 * Created by oddca on 02/04/2018.
 */
public class F7 implements Function {

    private int dimensions;
    private RandomGenerator random;

    // Shifted Sphere function
    public F7(int dimensions, RandomGenerator random){
        this.dimensions = dimensions;
        this.random = random;
    }

    @Override
    public double calculate(double[] input) {
        double sum = 0;
        for (int i = 0; i < dimensions; i++) {
            if (i == dimensions - 1){
                sum += fractal1D(input[i] + twist(input[0]));
            }
            else {
                sum += fractal1D(input[i] + twist(input[i+1]));
            }
        }
        return sum;
    }

    private double twist(double x){
        return 4*(Math.pow(x, 4) - 2*Math.pow(x, 3) + Math.pow(x, 2));
    }

    private double fractal1D(double x){
        double sum = 0.0;
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= Math.pow(2, i - 1); j++) {
                for (int k = 1; k <= random.nextInt(3); k++) {
                    sum += doubledip(x, random.nextDouble(), (1.0 / (Math.pow(2, k-1)*(2.0 - random.nextDouble()))));
                }
            }
        }
        return sum;
    }

    private double doubledip(double x, double c, double s){
        if (x < 0.5 && x > -0.5){
            return (-6144*Math.pow(x - c, 6) + 3088*Math.pow(x - c, 4) - 392*Math.pow(x - c, 2) + 1)*s;
        }
        else {
            return 0;
        }
    }

    @Override
    public double getUpperLimit() {
        return 1;
    }

    @Override
    public double getLowerLimit() {
        return -1;
    }

    @Override
    public int getDimensions() {
        return dimensions;
    }

    @Override
    public double getBias() {
        return 0;
    }

    @Override
    public String getName() {
        return "F7";
    }
}
