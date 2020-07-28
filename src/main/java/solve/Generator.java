package solve;

import main.Log;
import main.Verifier;
import structure.Grid;
import structure.Square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Adrian
 */
public class Generator {
    private final Random random = new Random();
    private ArrayList<Grid> parents;

    private int latestGeneration = 1, bestFitnessScore = 10000;

    private HashMap<Grid, Integer> reproductionPool;

    private Genetic_Algorithm GA;

    public Generator(Genetic_Algorithm GA) {
        this.GA = GA;
    }


    /**
     * This method will create a specified number of theoretically possible solutions to the puzzle.
     * Each solution is an individual and each non pre-solved square is a gene.
     * Each solution's genes will be randomly decided, according to the following two restrictions,
     * in their inception:
     * 1. Non pre-solved squares shall be populated with integers between 1 and 9.
     * 2. Non pre-solved squares shall not be populated with integers that conflict with the integers
     * in the pre-solved squares' respective rows, columns or regions.
     */
    public HashMap<Grid, Integer> produceFirstGeneration(Grid unsolved, int populationSize) {
        this.reproductionPool = new HashMap<>();

        // Adding as many grids as prescribed population size
        for (int i = 0; i < populationSize; i++) {
            Grid newGrid = new Grid();

            // Setting new grid's pre-solved squares to be identical to those of unsolved grid
            for (Square square : unsolved.getSquareList()) {
                if (square.getValue() != -1) {
                    newGrid.getSquareList().get(unsolved.getSquareList().indexOf(square)).setValue(square.getValue());
                    newGrid.getSquareList().get(unsolved.getSquareList().indexOf(square)).setPreSolved(true);
                }
            }

            // Checking each non pre-solved square's local pre-solved squares to set its new random value
            for (Square square : newGrid.getSquareList()) {
                if (!square.isPreSolved()) {

                    ArrayList<Integer> localPreSolvedValues = new ArrayList<>();

                    newGrid.getBandList().get(square.getBand()).getSquareList().forEach(
                            (n) -> localPreSolvedValues.add(n.isPreSolved() ? n.getValue() : 0));
                    newGrid.getStackList().get(square.getStack()).getSquareList().forEach(
                            (n) -> localPreSolvedValues.add(n.isPreSolved() ? n.getValue() : 0));
                    newGrid.getRegionList().get(square.getRegion()).getSquareList().forEach(
                            (n) -> localPreSolvedValues.add(n.isPreSolved() ? n.getValue() : 0));


                    int newGene = random.nextInt(9) + 1;
                    while (localPreSolvedValues.contains(newGene)) {
                        newGene = random.nextInt(9) + 1;
                    }

                    square.setValue(newGene);
                }
            }

            newGrid.setGeneration(1);
            this.reproductionPool.put(newGrid, Adjudicator.determineFitness(newGrid));
        }
        return this.reproductionPool;
    }

    /**
     * Performs reproduction between pairs of parents and inserts new individuals into reproduction pool
     *
     * @param reproductionPool Current reproduction pool
     * @param parents          List of parents
     * @param mutationRate     Percentage of non pre-solved squares to be randomly mutated after crossover
     * @param iteration        Current program iteration, for data purposes
     * @return Edited reproduction pool with new individuals integrated
     */
    public HashMap<Grid, Integer> reproduce(HashMap<Grid, Integer> reproductionPool, ArrayList<Grid> parents, double mutationRate, int iteration) {

        this.reproductionPool = reproductionPool;
        this.parents = parents;

        while (!this.parents.isEmpty()) {

            Grid pB = this.parents.remove(0), pA = this.parents.remove(0); // consider making parents a stack


            double unPreSolvedSquares = 0; // Collected in Crossover but used in Mutation

            // Crossover

            int crossoverPoint = random.nextInt(80); // Random crossover point between 0 and 80
            Grid newGrid = new Grid();

            for (int i = 0; i < crossoverPoint; i++) {
                newGrid.getSquareList().get(i).setValue(pA.getSquareList().get(i).getValue());
                if (pA.getSquareList().get(i).isPreSolved()) {
                    newGrid.getSquareList().get(i).setPreSolved(true);
                } else {
                    unPreSolvedSquares++;
                }
            }

            for (int i = crossoverPoint; i < 81; i++) {
                newGrid.getSquareList().get(i).setValue(pB.getSquareList().get(i).getValue());
                if (pB.getSquareList().get(i).isPreSolved()) {
                    newGrid.getSquareList().get(i).setPreSolved(true);
                } else {
                    unPreSolvedSquares++;
                }
            }


            // Mutation

            int mutationCount = (int) Math.round(mutationRate * unPreSolvedSquares), mutationPoint;
            ArrayList<Integer> mutationPoints = new ArrayList<>(mutationCount);

            // Create mutation points
            for (int i = 0; i < mutationCount; i++) {
                mutationPoint = random.nextInt(80); // Random mutation point between 0 and 80
                while (newGrid.getSquareList().get(mutationPoint).isPreSolved() ||
                        mutationPoints.contains(mutationPoint)) {
                    // Will not place point on pre-solved square or square already selected for mutation
                    mutationPoint = random.nextInt(80);
                }
                mutationPoints.add(mutationPoint);

                // Set mutation at respective mutation point
                newGrid.getSquareList().get(mutationPoint).setValue(random.nextInt(8) + 1); // Random mutation between 1 and 9
            }


            // Generations

            // Set new generation
            int genNext = Math.max(pA.getGeneration(), pB.getGeneration()) + 1;
            newGrid.setGeneration(genNext);

            // Update latest generation
            if (genNext > this.latestGeneration) {
                this.latestGeneration = genNext;
            }


            // Update Reproduction Pool

            // Find worst-scoring individual in reproduction pool
            Grid leastFit = null;
            int maxFitness = 0;

            for (Map.Entry<Grid, Integer> entry : this.reproductionPool.entrySet()) {
                if (entry.getValue() > maxFitness) {
                    maxFitness = entry.getValue();
                    leastFit = entry.getKey();
                }
            }

            // Replace worst-scoring individual with new individual
            int newFitness = Adjudicator.determineFitness(newGrid);

//            if (newFitness <= maxFitness) { // Replacement only occurs if new individual scores equal to or better than worst
//                this.reproductionPool.remove(leastFit);
//                this.reproductionPool.put(newGrid, newFitness);
//            }

            this.reproductionPool.remove(leastFit);
            this.reproductionPool.put(newGrid, newFitness);


            int currentBest = this.bestFitnessScore; // For testing purposes


            // Update best recorded fitness score, if applicable
            if (newFitness <= this.bestFitnessScore) {
                this.bestFitnessScore = newFitness;
            }


//            // For testing purposes
//
//---            if ((newFitness < currentBest)) {
//                Log.logger.info("New score: " + newFitness + " | Old Score: " + maxFitness + " | Generation: " + genNext + " | Iterations: " + iteration);
//                GA.makeImprovement();
// ----           }
//
//            if (displayGrids) {
//                newGrid.displayGrid(false);
//            }
        }
        return this.reproductionPool;
    }
}
