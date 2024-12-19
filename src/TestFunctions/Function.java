package TestFunctions;

/**
 * Created by oddca on 23/03/2018.
 */
public interface Function {

    double calculate(double[] input);

    double getUpperLimit();

    double getLowerLimit();

    int getDimensions();

    double getBias();

    String getName();
}
