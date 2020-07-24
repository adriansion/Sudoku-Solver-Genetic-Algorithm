package solve;

import main.Log;
import structure.Grid;
import structure.Square;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * First attempt at a solving algorithm using a stochastic technique.
 * This algorithm is genetic.
 *
 * @author Adrian
 */
public class Genetic_Algorithm {

    private final int individuals = 100, iterationLimit = 500000;
    private final double mutationRate = 0.05;

    private int latestGeneration = 1, bestFitnessScore = 10000, earlyFitnessScore = 10000, iterations = 0;
    private Grid unsolved, solution, fittestA = null, fittestB = null;

    // Testing configurations
    boolean printData = false;
    boolean printScores = false;
    boolean displayGrids = false;
    boolean limitIterations = true;


    private final HashMap<Grid, Integer> reproductionPool = new HashMap<>();
    private final Random random = new Random();


    public Grid solve(Grid grid) {
        this.unsolved = grid;

        this.produceFirstGeneration(this.individuals);

        reproductionPool.forEach((n, m) -> reproductionPool.put(n, this.determineFitness(n)));
        Log.logger.info("Production of first generation complete.");

        if (limitIterations) {
            for (iterations = 1; iterations <= iterationLimit; iterations++) {
                this.runTournament();
                this.reproduce();
                if (bestFitnessScore != 10000 && earlyFitnessScore == 10000) {
                    earlyFitnessScore = bestFitnessScore;
                }
            }
        } else {
            while (this.bestFitnessScore != 0) {
                iterations++;
                this.runTournament();
                this.reproduce();
            }
        }


        Log.logger.info("Sample Early Score: " + this.earlyFitnessScore);
        Log.logger.info("Best Fitness Score: " + this.bestFitnessScore);
        Log.logger.info("Latest Generation: " + this.latestGeneration);


        return this.solution;
    }

    /**
     * This method will create a specified number of theoretically possible solutions to the puzzle.
     * Each solution is an individual and each non-presolved square is a gene.
     * Each solution's genes will be randomly decided, according to the following two restrictions,
     * in their inception:
     * 1. Non-presolved squares shall be populated with integers between 1 and 9.
     * 2. Non-presolved squares shall not be populated with integers that conflict with the integers
     * in the presolved squares' respective rows, columns or regions.
     */
    private void produceFirstGeneration(int individuals) {
        for (int i = 0; i < individuals; i++) {
            Grid newGrid = new Grid();

            for (Square square : this.unsolved.getSquareList()) {
                if (square.getValue() != -1) {
                    newGrid.getSquareList().get(this.unsolved.getSquareList().indexOf(square)).setValue(square.getValue());
                    newGrid.getSquareList().get(this.unsolved.getSquareList().indexOf(square)).setPreSolved(true);
                }
            }

            for (Square square : newGrid.getSquareList()) {
                if (!square.isPreSolved()) {

                    ArrayList<Integer> preSolvedSquares = new ArrayList<>();

                    for (Square square1 : newGrid.getBandList().get(square.getBand()).getSquareList()) {
                        if (square1.isPreSolved()) {
                            preSolvedSquares.add(square1.getValue());
                        }
                    }

                    for (Square square1 : newGrid.getStackList().get(square.getStack()).getSquareList()) {
                        if (square1.isPreSolved()) {
                            preSolvedSquares.add(square1.getValue());
                        }
                    }

                    for (Square square1 : newGrid.getRegionList().get(square.getRegion()).getSquareList()) {
                        if (square1.isPreSolved()) {
                            preSolvedSquares.add(square1.getValue());
                        }
                    }


                    int newGene = random.nextInt(9) + 1;
                    while (preSolvedSquares.contains(newGene)) {
                        newGene = random.nextInt(9) + 1;
                    }

                    square.setValue(newGene);
                }
            }

            newGrid.setGeneration(1);
            reproductionPool.put(newGrid, 101);
        }
    }

    /**
     * Assigns a numerical fitness score to each grid in the reproduction pool.
     */
    private int determineFitness(Grid grid) {

        // Data Gathering

        // A hashmap containing value counts is initialized for each
        // of 27 grid structures (9 bands, 9 stacks, 9 regions)
        ArrayList<HashMap<Integer, Integer>> bandValueCounts = new ArrayList<>();
        ArrayList<HashMap<Integer, Integer>> stackValueCounts = new ArrayList<>();
        ArrayList<HashMap<Integer, Integer>> regionValueCounts = new ArrayList<>();

        // Populating array lists with hashmaps
        for (int i = 1; i <= 9; i++) { // Structure
            bandValueCounts.add(new HashMap<>());
            stackValueCounts.add(new HashMap<>());
            regionValueCounts.add(new HashMap<>());

            // Start by setting every value count to -1
            for (int j = 1; j <= 9; j++) { // Hashmap
                bandValueCounts.get(bandValueCounts.size() - 1).put(j, -1);
                stackValueCounts.get(stackValueCounts.size() - 1).put(j, -1);
                regionValueCounts.get(regionValueCounts.size() - 1).put(j, -1);
            }
        }

        // Setting actual value counts for each square in each structure
        for (int i = 0; i < 9; i++) {
            for (Square square : grid.getBandList().get(i).getSquareList()) {
                bandValueCounts.get(i).put(square.getValue(), bandValueCounts.get(i).get(square.getValue()) + 1);
            }
            for (Square square : grid.getStackList().get(i).getSquareList()) {
                stackValueCounts.get(i).put(square.getValue(), stackValueCounts.get(i).get(square.getValue()) + 1);
            }
            for (Square square : grid.getRegionList().get(i).getSquareList()) {
                regionValueCounts.get(i).put(square.getValue(), regionValueCounts.get(i).get(square.getValue()) + 1);
            }
        }


        // Score Assignment

        ArrayList<Integer> multipliers = new ArrayList<>();

        // Raw value count data is added to multipliers array list
        for (int i = 0; i < 9; i++) { // Structure
            for (int j = 1; j <= 9; j++) { // Hashmap
                multipliers.add(bandValueCounts.get(i).get(j));
                multipliers.add(stackValueCounts.get(i).get(j));
                multipliers.add(regionValueCounts.get(i).get(j));
            }
        }

        // Score is assigned based on missing and duplicated values
        int score = 0;
        for (Integer multiplier : multipliers) {
            score += multiplier == -1 ? 10 : multiplier * 10;
        }

        if (printScores) {
            System.out.println("Score :" + score);
        }
        return score;
    }

    /**
     * Determines the individuals to reproduce and crossover.
     */
    private void runTournament() {

        // Organize sub-population

        HashMap<Grid, Integer> subPop = new HashMap<>();
        ArrayList<Map.Entry<Grid, Integer>> poolGrids = new ArrayList<>(reproductionPool.entrySet());

        for (int i = 0; i < this.individuals / 2; i++) {
            int index = random.nextInt(poolGrids.size());
            subPop.put(poolGrids.get(index).getKey(), poolGrids.get(index).getValue());
            poolGrids.remove(index);
        }


        // Select parents from sub-population

        ArrayList<Map.Entry<Grid, Integer>> subPopGrids = new ArrayList<>(subPop.entrySet());
        subPopGrids.sort(Map.Entry.comparingByValue());

        this.fittestA = poolGrids.get(0).getKey();
        this.fittestB = poolGrids.get(1).getKey();

    }

    /**
     * Creates offspring to return to populace
     */
    private void reproduce() {

        double unPreSolvedSquares = 0; // Collected in Crossover but used in Mutation

        // Crossover

        int crossoverPoint = random.nextInt(80); // Random crossover point between 0 and 80
        Grid newGrid = new Grid();

        for (int i = 0; i < crossoverPoint; i++) {
            newGrid.getSquareList().get(i).setValue(fittestA.getSquareList().get(i).getValue());
            if (fittestA.getSquareList().get(i).isPreSolved()) {
                newGrid.getSquareList().get(i).setPreSolved(true);
            } else {
                unPreSolvedSquares++;
            }
        }

        for (int i = crossoverPoint; i < 81; i++) {
            newGrid.getSquareList().get(i).setValue(fittestB.getSquareList().get(i).getValue());
            if (fittestB.getSquareList().get(i).isPreSolved()) {
                newGrid.getSquareList().get(i).setPreSolved(true);
            } else {
                unPreSolvedSquares++;
            }
        }


        // Mutation

        int mutationCount = (int) Math.round(this.mutationRate * unPreSolvedSquares), mutationPoint;
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
        int genNext = Math.max(fittestA.getGeneration(), fittestB.getGeneration()) + 1;
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
        int newFitness = this.determineFitness(newGrid);

        if (newFitness <= maxFitness) { // Replacement only occurs if new individual scores equal to or better than worst
            this.reproductionPool.remove(leastFit);
            this.reproductionPool.put(newGrid, newFitness);
        }

        // Update best recorded fitness score, if applicable
        if (newFitness <= this.bestFitnessScore) {
            this.bestFitnessScore = newFitness;
        }

        // Update solution, if applicable
        if (this.bestFitnessScore == 0) {
            this.solution = newGrid;
        }


        // For testing purposes
        if (printData) {
            System.out.println("new: " + newFitness + " old: " + maxFitness + " Latest gen: " + this.latestGeneration + " Iterations: " + this.iterations);
        }
        if (displayGrids) {
            newGrid.displayGrid(false);
        }
    }
}
