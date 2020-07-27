package solve;

import main.Log;
import structure.Grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * First attempt at a solving algorithm using a stochastic technique.
 * This algorithm is genetic.
 * <p>
 * Note
 * - early sample fitness
 *
 * @author Adrian
 */
public class Genetic_Algorithm {

    // Configuration

    private final int populationSize = 100;
    private final int iterationLimit = 500000;

    private int iterations = 0;
    private int latestGeneration = 1;
    private int bestFitnessScore = 10000;

    private final double mutationRate = 0.05;

    boolean printData = true;
    boolean printAllData = false;
    boolean printScores = false;
    boolean displayGrids = false;
    boolean limitIterations = true;


    // Objects

    private final Selector selector = new Selector();
    private final Generator generator = new Generator();

    private Grid solution;


    // Consider having this returned from tournament, and passed to reproduce
    private ArrayList<Grid> parents;


    public Grid solve(Grid unsolved) {

        HashMap<Grid, Integer> reproductionPool = generator.produceFirstGeneration(unsolved, this.populationSize);
        Log.logger.info("Production of first generation complete.");

        if (this.limitIterations) {
            for (this.iterations = 1; this.iterations <= this.iterationLimit; this.iterations++) {
                this.parents = this.selector.runTournament(this.populationSize, reproductionPool); // use this for new array list
                reproductionPool = this.generator.reproduce(reproductionPool, this.parents, this.mutationRate, this.iterations);

//                if (iterations % (iterationLimit / 100) == 0) {
//                    System.out.println("Iterations: " + (int) (((double) iterations / (double) iterationLimit) * 100) + "% complete");
//                }
            }
        } else {
            while (this.bestFitnessScore != 0) {
                iterations++;
                selector.runTournament(this.populationSize, reproductionPool);
                generator.reproduce(reproductionPool, this.parents, this.mutationRate, this.iterations);
            }
        }

        Log.logger.info("Best Fitness Score: " + this.bestFitnessScore);
        Log.logger.info("Latest Generation: " + this.latestGeneration);


        return this.solution;
    }
}
