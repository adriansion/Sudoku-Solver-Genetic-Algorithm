package solve;

import main.Log;
import structure.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * First attempt at a solving algorithm using a stochastic technique.
 * This algorithm is genetic.
 *
 * @author Adrian
 */
public class Genetic_Algorithm {

    private final int individuals = 100, mutations = 3;
    private int latestGeneration = 1, bestFitnessScore = 10000, earlyFitnessScore = 10000, iterations = 0;
    private Grid unsolved, solution, fittestPrimary = null, fittestSecondary = null;
    boolean printData = false, compressScores = false, expandScores = true, printScores = false, displayGrids = false;
    private HashMap<Grid, Integer> reproductionPool = new HashMap<>();
    Random random = new Random();

    public Grid solve(Grid grid) {
        this.unsolved = grid;

        this.produceFirstGeneration(this.individuals);

        reproductionPool.forEach((n, m) -> reproductionPool.put(n, this.determineFitness(n)));
        Log.logger.info("Production of first generation complete.");


//        while (this.bestFitnessScore != 0) {
//            iterations++;
//            this.runTournament();
//            this.reproduce();
//
//        }

        for (iterations = 1; iterations <= 1000000; iterations++) {
            this.runTournament();
            this.reproduce();
            if (bestFitnessScore != 10000 && earlyFitnessScore == 10000) {
                earlyFitnessScore = bestFitnessScore;
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
     * - two points added per duplicated value per row, column and region
     *
     * @param grid
     * @return
     */
    private int determineFitness(Grid grid) {
        int score = 0;
        ArrayList<HashMap<Integer, Integer>> bandFitness = new ArrayList<>();
        ArrayList<HashMap<Integer, Integer>> stackFitness = new ArrayList<>();
        ArrayList<HashMap<Integer, Integer>> regionFitness = new ArrayList<>();

        for (int i = 1; i <= 9; i++) {
            bandFitness.add(new HashMap<>());
            stackFitness.add(new HashMap<>());
            regionFitness.add(new HashMap<>());

            for (int j = 1; j <= 9; j++) {
                bandFitness.get(bandFitness.size() - 1).put(j, -1);
            }
            for (int j = 1; j <= 9; j++) {
                stackFitness.get(stackFitness.size() - 1).put(j, -1);
            }
            for (int j = 1; j <= 9; j++) {
                regionFitness.get(regionFitness.size() - 1).put(j, -1);
            }
        }

        for (int i = 0; i < grid.getBandList().size(); i++) {
            for (Square square : grid.getBandList().get(i).getSquareList()) {
                bandFitness.get(i).put(square.getValue(), bandFitness.get(i).get(square.getValue()) + 1);
            }
        }
        for (int i = 0; i < grid.getStackList().size(); i++) {
            for (Square square : grid.getStackList().get(i).getSquareList()) {
                stackFitness.get(i).put(square.getValue(), stackFitness.get(i).get(square.getValue()) + 1);
            }
        }
        for (int i = 0; i < grid.getRegionList().size(); i++) {
            for (Square square : grid.getRegionList().get(i).getSquareList()) {
                regionFitness.get(i).put(square.getValue(), regionFitness.get(i).get(square.getValue()) + 1);
            }
        }

//        for (HashMap<Integer, Integer> h : bandFitness) {
//            System.out.println(h.toString());
//        }

        for (int i = 0; i < 9; i++) {
            for (int j = 1; j <= 9; j++) {
                int multiplier = bandFitness.get(i).get(j);
                if (multiplier == -1) {
                    score += 5;
                } else {
                    score += multiplier * 3;
                }
                multiplier = stackFitness.get(i).get(j);
                if (multiplier == -1) {
                    score += 5;
                } else {
                    score += multiplier * 3;
                }
                multiplier = regionFitness.get(i).get(j);
                if (multiplier == -1) {
                    score += 5;
                } else {
                    score += multiplier * 3;
                }
            }
        }

        if (compressScores) {
            score /= 4;
        }
        if (expandScores) {
            score *= 5;
        }
        if (printScores) {
            System.out.println("Score :" + score);
        }
        return score;

        // This is for testing purposes only, but could be worked into a fitness function!
//        for (Grid g : reproductionPool) {
//            int[] counts = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
//            for (Square square : g.getSquareList()) {
//                counts[square.getValue() - 1]++;
//            }
//            for (int k = 0; k < 9; k++){
//                System.out.println(counts[k]);
//
//            }
//            System.out.println();
//        }
    }

    /**
     * Determines the individuals to reproduce and crossover.
     */
    private void runTournament() {

        HashMap<Grid, Integer> subPopA = new HashMap<>(), subPopB = new HashMap<>();
        this.reproductionPool.forEach((n, m) -> { // Condense this to eliminate subPopB
            if (subPopA.size() < this.individuals / 2 && random.nextInt(1) == 1) {
                subPopA.put(n, m);
            } else if (subPopB.size() == this.individuals / 2) {
                subPopA.put(n, m);
            } else {
                if (random.nextInt(1) == 1) {
                    subPopA.put(n, m);
                } else {
                    subPopB.put(n, m);
                }
            }

        });

        // Starting with full Elitism
        ArrayList<Integer> fitnesses = new ArrayList<>();
        subPopA.forEach((n, m) -> fitnesses.add(m));
        fitnesses.sort(null);
//        fitnesses.forEach(System.out::println);


        for (Map.Entry<Grid, Integer> entry : subPopA.entrySet()) {
            if (entry.getValue().equals(fitnesses.get(0))) {
                this.fittestPrimary = entry.getKey();
//                System.out.println("Primary Fitness Score: " + entry.getValue());
                break;
            }
        }
        for (Map.Entry<Grid, Integer> entry : subPopA.entrySet()) {
            if (entry.getValue().equals(fitnesses.get(1)) && !entry.getKey().equals(fittestPrimary)) {
                this.fittestSecondary = entry.getKey();
//                System.out.println("Secondary Fitness Score: " + entry.getValue());
                break;
            }
        }

//        System.out.println( fittestPrimary + " " + fittestSecondary);


//        subPopA.forEach((n, m) -> System.out.println("A: " + n + " " + m));
//        System.out.println();
//        subPopB.forEach((n, m) -> System.out.println("B: " + n + " " + m));
//        System.out.println(subPopA.size() + " " + subPopB.size());


    }

    /**
     * Creates offspring to return to populace
     */
    private void reproduce() {
        // Crossover

        int crossoverPoint = random.nextInt(fittestPrimary.getSquareList().size() - 1) + 1;
        Grid newGrid = new Grid();

        for (int i = 0; i < crossoverPoint; i++) {
            newGrid.getSquareList().get(i).setValue(fittestPrimary.getSquareList().get(i).getValue());
            newGrid.getSquareList().get(i).setPreSolved(fittestPrimary.getSquareList().get(i).isPreSolved());
        }

        for (int i = crossoverPoint; i < fittestSecondary.getSquareList().size(); i++) {
            newGrid.getSquareList().get(i).setValue(fittestSecondary.getSquareList().get(i).getValue());
            newGrid.getSquareList().get(i).setPreSolved(fittestPrimary.getSquareList().get(i).isPreSolved());
        }


        // Mutation

        int mutationCount = 0;
        int[] mutationPoints = new int[this.mutations];
        for (int i = 0; i < mutationPoints.length; i++) {
            mutationPoints[i] = random.nextInt(80);
        }

        for (int i = 0; i < mutationPoints.length; i++) {
            if (!newGrid.getSquareList().get(mutationPoints[i]).isPreSolved()) {
                newGrid.getSquareList().get(mutationPoints[i]).setValue(random.nextInt(8) + 1);
            }
        }

        // Handle Generations

        int genA = fittestPrimary.getGeneration();
        int genB = fittestSecondary.getGeneration();
        int genNext = Math.max(genA, genB) + 1;
        newGrid.setGeneration(genNext);
        if (genNext > this.latestGeneration) {
            this.latestGeneration = genNext;
        }

//        newGrid.displayGrid(false);
//        System.out.println(this.determineFitness(newGrid));

        // Return new grid to reproductionPool

        AtomicInteger maxFitness = new AtomicInteger();
        this.reproductionPool.forEach((n, m) -> {
            if (m > maxFitness.get()) {
                maxFitness.set(m);
            }
        });
        Grid unfittest = null;
        for (Map.Entry<Grid, Integer> entry : this.reproductionPool.entrySet()) {
            if (entry.getValue() == maxFitness.intValue()) {
                unfittest = entry.getKey();
                break;

            }
        }
        int newFitness = this.determineFitness(newGrid);
        if (printData) {
            System.out.println("new: " + newFitness + " old: " + maxFitness.intValue() + " Latest gen: " + this.latestGeneration + " Iterations: " + this.iterations);
        }
        if (newFitness <= maxFitness.intValue()) {
            this.reproductionPool.remove(unfittest);
            this.reproductionPool.put(newGrid, this.determineFitness(newGrid));

            if (displayGrids) {
                newGrid.displayGrid(false);
            }

            if (newFitness <= this.bestFitnessScore) {
                this.bestFitnessScore = newFitness;
            }
            if (this.bestFitnessScore == 0) {
                this.solution = newGrid;
            }
        }
    }
}
