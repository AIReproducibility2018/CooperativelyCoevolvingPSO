import java.util.Random;

/**
 * Created by oddca on 25/03/2018.
 */
public class Particle {

    private double[] position;
    private double[] personalBest;
    private double[] localBest;
    private double currentValue;
    private double personalBestValue;

    public Particle(double[] position, double[] personalBest){
        this.position = position;
        this.personalBest = personalBest;
        this.localBest = personalBest.clone();
    }

    public double[] getPosition(){
        return position;
    }

    public double[] getPersonalBest(){
        return personalBest;
    }

    public double[] getLocalBest(){
        return localBest;
    }

    public double getPersonalBestValue(){
        return personalBestValue;
    }

    public double getCurrentValue(){
        return currentValue;
    }

    public void setPosition(double[] newPosition){
        this.position = newPosition;
    }

    public void setPersonalBest(double[] newBest){
        this.personalBest = newBest;
    }

    public void setLocalBest(double[] localBest){
        this.localBest = localBest;
    }

    public void setCurrentValue(double currentValue){
        this.currentValue = currentValue;
    }

    public void setPersonalBestValue(double personalBestValue){
        this.personalBestValue = personalBestValue;
    }
}
