package solve;

import main.Log;
import structure.Grid;

import java.util.ArrayList;
import java.util.HashMap;

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

    private final int populationSize = 100;
    private final int iterationLimit = 30000;

    private int iteration = 0;
    private int latestGeneration = 1;
    private int bestFitnessScore = 10000;

    private int iterationsSinceLastImprovement = 0;

    private final double mutationRate = 0.05;

    boolean printData = true;
    boolean printAllData = false;
    boolean limitIterations = true;


    private final Selector selector = new Selector();
    private final Generator generator = new Generator(this);

    private Grid solution = null;

    private ArrayList<Grid> parents;


    /**
     * @param unsolved Grid whose solution is being searched for
     * @return Possible solution to puzzle
     */
    public Grid solve(Grid unsolved, int completedIterations) {
        int completedIterations1 = completedIterations;

        HashMap<Grid, Integer> reproductionPool = generator.produceFirstGeneration(unsolved, this.populationSize);
//        Log.logger.info("Production of first generation complete.");

        if (this.limitIterations) {
            for (this.iteration = 1; this.iteration <= this.iterationLimit; this.iteration++) {
                completedIterations1++;
                this.parents = this.selector.runTournament(this.populationSize, reproductionPool); // use this for new array list
                reproductionPool = this.generator.reproduce(reproductionPool, this.parents, this.mutationRate, this.iteration);
                this.solution = selector.checkForSolution(reproductionPool);
                if (this.solution != null) {
                    Log.logger.info("Solution found. Iteration: " + this.iteration + ", " + completedIterations1);
                    break;
                }

                if (completedIterations1 > 1500000) {
                    System.out.println("No solution found on " + Thread.currentThread().toString());
                    break;
                }

                this.iterationsSinceLastImprovement++;
                if (iterationsSinceLastImprovement > 20000) {
                    this.makeImprovement();
//                    this.iteration = 1;
//                    reproductionPool = generator.produceFirstGeneration(unsolved, this.populationSize);
//                    Log.logger.info("Reset reproduction pool -- application likely stuck in local minimum.");
                    this.solve(unsolved, completedIterations1);
                    break;
                }


//                if (iterations % (iterationLimit / 100) == 0) {
//                    System.out.println("Iterations: " + (int) (((double) iterations / (double) iterationLimit) * 100) + "% complete");
//                }
            }
        } else {
            while (this.bestFitnessScore != 0) {
                iteration++;
                selector.runTournament(this.populationSize, reproductionPool);
                generator.reproduce(reproductionPool, this.parents, this.mutationRate, this.iteration);
            }
        }

//        for (this.iteration = 1; this.iteration <= this.iterationLimit; this.iteration++) {
//            this.parents = this.selector.runTournament(this.populationSize, reproductionPool);
//            reproductionPool = this.generator.reproduce(reproductionPool, this.parents, this.mutationRate, this.iteration);
//            this.solution = selector.checkForSolution(reproductionPool);
//            if (this.solution != null) {
//                Log.logger.info("Solution found. Iteration: " + this.iteration);
//                break;
//            }
//        }

        return this.solution;
    }

    public void makeImprovement() {
        this.iterationsSinceLastImprovement = 0;
    }
}
