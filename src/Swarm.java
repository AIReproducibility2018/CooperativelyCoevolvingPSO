import java.util.Random;

/**
 * Created by oddca on 25/03/2018.
 */
public class Swarm {

    private int[] indices;
    private Particle[] particles;
    private int swarmSize;
    private double[] swarmBest;
    private double swarmBestValue;

    public Swarm(Particle[] particles, int swarmSize, int[] indices){
        this.swarmSize = swarmSize;
        this.indices = indices;
        this.particles = particles;
        swarmBest = particles[0].getPersonalBest();
    }

    public Particle getParticle(int i){
        return particles[i];
    }

    public int getSwarmSize(){
        return swarmSize;
    }

    public double[] getSwarmBest(){
        return swarmBest;
    }

    public int[] getIndices(){
        return indices;
    }

    public double getSwarmBestValue(){
        return swarmBestValue;
    }

    public void setSwarmBest(double[] newBest){
        this.swarmBest = newBest;
    }

    public void setSwarmBestValue(double swarmBestValue){
        this.swarmBestValue = swarmBestValue;
    }
}
