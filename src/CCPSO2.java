import TestFunctions.Function;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.random.RandomGenerator;

/**
 * Created by oddca on 23/03/2018.
 */
public class CCPSO2 {

    public static double[] run(Function function, int[] S, int max_fes, int swarmSize, double p, RandomGenerator random, boolean verbose){
        // Initialize initial variables
        int n = function.getDimensions();
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        double[] globalBest = new double[n];
        Arrays.fill(globalBest, 0.0);
        double globalBestValue;
        int fes = 0;

        // Initialize particles with random positions
        double[][] X = new double[swarmSize][n];
        double[][] Y = new double[swarmSize][n];
        for (int i = 0; i < swarmSize; i++) {
            for (int j = 0; j < n; j++) {
                double value = function.getLowerLimit() + (function.getUpperLimit() - function.getLowerLimit())*random.nextDouble();
                X[i][j] = value;
                Y[i][j] = value;
            }
        }

        // Create K swarms
        int r = random.nextInt(S.length);
        int s = S[r];
        if (verbose){
            System.out.println("s: " + s);
        }
        int K = n/s;
        int[] permuted = permuteIndices(indices, random);
        ArrayList<Swarm> swarms = new ArrayList<Swarm>();
        for (int i = 0; i < K; i++) {
            int[] swarmIndices = new int[s];
            for (int j = 0; j < s; j++) {
                int k = i*s + j;
                swarmIndices[j] = permuted[k];
            }
            Particle[] particles = new Particle[swarmSize];
            for (int j = 0; j < swarmSize; j++) {
                double[] particlePosition = new double[s];
                double[] particleBest = new double[s];
                for (int k = 0; k < s; k++) {
                    particlePosition[k] = X[j][swarmIndices[k]];
                    particleBest[k] = Y[j][swarmIndices[k]];
                }
                particles[j] = new Particle(particlePosition, particleBest);
            }
            Swarm swarm = new Swarm(particles, swarmSize, swarmIndices);
            swarms.add(swarm);

            globalBest = insert(globalBest, swarm.getSwarmBest(), i, s);
        }

        // Calculate and set initial fitness values
        for (int i = 0; i < K; i++) {
            Swarm swarm = swarms.get(i);
            for (int j = 0; j < swarmSize; j++) {
                Particle particle = swarm.getParticle(j);
                double fitness = function.calculate(insert(globalBest, particle.getPosition(), i, s));
                particle.setCurrentValue(fitness);
                particle.setPersonalBestValue(fitness);
                if (j == 0){
                    swarm.setSwarmBestValue(fitness);
                }
            }
        }

        globalBestValue = function.calculate(globalBest);
        boolean improvement = true;

        // Main loop
        while (fes < max_fes) {
            if (verbose){
                System.out.println("FES: " + fes + ". Current best: " + (globalBestValue - function.getBias()) + "\n");
            }
            // If no improvement observed, reconfigure swarms
            if (!improvement){
                r = random.nextInt(S.length);
                s = S[r];
                if (verbose){
                    System.out.println("s: " + s + "\n");
                }
                K = n/s;
                permuted = permuteIndices(indices, random);
                swarms.clear();
                for (int j = 0; j < K; j++) {
                    int[] swarmIndices = new int[s];
                    for (int k = 0; k < s; k++) {
                        int m = j*s + k;
                        swarmIndices[k] = permuted[m];
                    }
                    Particle[] particles = new Particle[swarmSize];
                    double bestParticleValue = Double.MAX_VALUE;
                    Particle bestParticle = null;
                    for (int k = 0; k < swarmSize; k++) {
                        double[] particlePosition = new double[s];
                        double[] particleBest = new double[s];
                        for (int l = 0; l < s; l++) {
                            particlePosition[l] = X[k][swarmIndices[l]];
                            particleBest[l] = Y[k][swarmIndices[l]];
                        }
                        Particle particle = new Particle(particlePosition, particleBest);
                        double fitness = function.calculate(insert(globalBest, particle.getPosition(), j, s));
                        double bestFitness = function.calculate(insert(globalBest, particle.getPersonalBest(), j, s));
                        particle.setCurrentValue(fitness);
                        particle.setPersonalBestValue(bestFitness);
                        particles[k] = particle;
                        if (bestFitness < bestParticleValue){
                            bestParticleValue = bestFitness;
                            bestParticle = particle;
                        }
                    }
                    Swarm swarm = new Swarm(particles, swarmSize, swarmIndices);
                    swarm.setSwarmBest(bestParticle.getPersonalBest().clone());
                    swarm.setSwarmBestValue(bestParticleValue);
                    swarms.add(swarm);
                }
            }

            // Reset improvement
            improvement = false;

            // Update personal, swarm and global best
            for (int j = 0; j < swarms.size(); j++) {
                Swarm swarm = swarms.get(j);
                int[] swarmIndices = swarm.getIndices();
                // Update personal and swarm best
                for (int k = 0; k < swarm.getSwarmSize(); k++) {
                    Particle particle = swarm.getParticle(k);
                    fes += 1;
                    double newValue = function.calculate(insert(globalBest, particle.getPosition(), j, s));
                    particle.setCurrentValue(newValue);
                    if (newValue < particle.getPersonalBestValue()){
                        particle.setPersonalBest(particle.getPosition().clone());
                        particle.setPersonalBestValue(newValue);
                        // Update best matrix, Y
                        for (int l = 0; l < s; l++) {
                            Y[k][swarmIndices[l]] = particle.getPersonalBest()[l];
                        }
                    }
                    if (particle.getPersonalBestValue() < swarm.getSwarmBestValue()){
                        swarm.setSwarmBest(particle.getPersonalBest().clone());
                        swarm.setSwarmBestValue(particle.getPersonalBestValue());
                    }
                }
                // Find local best for each particle
                for (int k = 0; k < swarm.getSwarmSize(); k++) {
                    Particle particle = swarm.getParticle(k);
                    Particle left;
                    if (k == 0){
                        left = swarm.getParticle(swarm.getSwarmSize()-1);
                    }
                    else {
                        left = swarm.getParticle(k-1);
                    }
                    Particle right;
                    if (k == swarm.getSwarmSize() - 1){
                        right = swarm.getParticle(0);
                    }
                    else {
                        right = swarm.getParticle(k+1);
                    }
                    Particle localBest = findLocalBest(left, particle, right);
                    particle.setLocalBest(localBest.getPosition().clone());
                }
                // Update global best
                if (swarm.getSwarmBestValue() < globalBestValue){
                    globalBest = insert(globalBest, swarm.getSwarmBest(), j, s);
                    globalBestValue = swarm.getSwarmBestValue();
                    improvement = true;
                }
            }

            // Update position of all particles using either Cauchy or Gaussian distribution
            for (int j = 0; j < swarms.size(); j++) {
                Swarm swarm = swarms.get(j);
                int[] swarmIndices = swarm.getIndices();
                for (int k = 0; k < swarmSize; k++) {
                    Particle particle = swarm.getParticle(k);
                    double[] particlePosition = new double[s];
                    for (int l = 0; l < s; l++) {
                        double oldPosition = particlePosition[l];
                        double rand = random.nextDouble();
                        double difference = Math.abs(particle.getPersonalBest()[l] - particle.getLocalBest()[l]);
                        double newPosition;
                        if (rand <= p){
                            if (difference > 0){
                                CauchyDistribution cauchyDistribution = new CauchyDistribution(random, 1, difference);
                                newPosition = particle.getPersonalBest()[l] + cauchyDistribution.sample()*difference;
                            }
                            else {
                                newPosition = particle.getPersonalBest()[l];
                            }
                        }
                        else {
                            newPosition = particle.getLocalBest()[l] + random.nextGaussian()*difference;
                        }

                        // Ensure new value not out of bounds

                        if (newPosition > function.getUpperLimit()){
                            newPosition = oldPosition + (function.getUpperLimit() - oldPosition)*random.nextDouble();
                        }
                        else if (newPosition < function.getLowerLimit()){
                            newPosition = function.getLowerLimit() + (oldPosition - function.getLowerLimit())*random.nextDouble();
                        }

                        X[k][swarmIndices[l]] = newPosition;
                        particlePosition[l] = newPosition;
                    }
                    particle.setPosition(particlePosition);
                }
            }
        }
        return globalBest;
    }

    private static double[] insert(double[] total, double[] section, int[] indices){
        double[] ret = total.clone();
        for (int i = 0; i < section.length; i++) {
            ret[indices[i]] = section[i];
        }
        return ret;
    }

    private static double[] insert(double[] total, double[] section, int swarm, int s){
        double[] ret = total.clone();
        for (int i = 0; i < s; i++) {
            int newPos = swarm*s + i;
            ret[newPos] = section[i];
        }
        return ret;
    }

    private static Particle findLocalBest(Particle left, Particle centre, Particle right){
        Particle localBest = centre;
        if (left.getCurrentValue() < localBest.getCurrentValue()){
            localBest = left;
        }
        if (right.getCurrentValue() < localBest.getCurrentValue()){
            localBest = right;
        }
        return localBest;
    }

    private static int[] permuteIndices(int[] indices, RandomGenerator random){
        int[] permuted = indices.clone();
        int index;
        int temp;
        for (int i = permuted.length-1; i > 0; i--) {
            index = random.nextInt(i+1);
            temp = permuted[index];
            permuted[index] = permuted[i];
            permuted[i] = temp;
        }
        return permuted;
    }

}
