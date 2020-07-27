package solve;

import structure.Grid;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Adrian
 */
public class Selector {

    Random random = new Random();
    HashMap<Grid, Integer> reproductionPool;

    /**
     * Determines the individuals to reproduce and crossover.
     */
    public ArrayList<Grid> runTournament(int populationSize, HashMap<Grid, Integer> reproductionPool) {

        ArrayList<Grid> parents = new ArrayList<>();

        int parentCount = 4;


        ArrayList<Map.Entry<Grid, Integer>> rPoolEntries = new ArrayList<>(reproductionPool.entrySet());
        HashMap<Grid, Integer> subPopulation = new HashMap<>();


        for (int i = 0; i < parentCount; i++) {

            // Get sub-population

            subPopulation.clear();
            for (int j = 0; j < populationSize / 2; j++) {
                int contestantIndex = random.nextInt(rPoolEntries.size());
                subPopulation.put(rPoolEntries.get(contestantIndex).getKey(), rPoolEntries.get(contestantIndex).getValue());

            }


            // Take fittest individual from sub-population to become new parent

            ArrayList<Map.Entry<Grid, Integer>> subPopEntries = new ArrayList<>(subPopulation.entrySet());
            subPopEntries.sort(Map.Entry.comparingByValue());
            Grid newParent = subPopEntries.get(0).getKey();

            parents.add(newParent);
            rPoolEntries.remove(newParent);

        }

        return parents;
    }

    /**
     * Used to check existing reproduction pool for a possible solution
     *
     * @param reproductionPool Reproduction pool in its current state
     * @return Grid whose fitness score is 0, null if grid not found
     */
    public Grid checkForSolution(HashMap<Grid, Integer> reproductionPool) {
        for (Map.Entry<Grid, Integer> entry : reproductionPool.entrySet()) {
            if (entry.getValue() == 0) {
                return entry.getKey();
            }
        }
        return null;
    }
}
