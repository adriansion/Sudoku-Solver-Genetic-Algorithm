package solve;

import structure.Grid;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Selector {

    Random random = new Random();

    /**
     * Determines the individuals to reproduce and crossover.
     */
    public ArrayList<Grid> runTournament(int populationSize, HashMap<Grid, Integer> reproductionPool) { // This could be parallelized
        // Could the array lists be replaced by some direct reference to entry sets?

        ArrayList<Grid> parents = new ArrayList<>();

        // Determine how many parents to have reproduce, based on the complete population size
        int parentCount = 2;//this.individuals / 25;
        if (((double) parentCount) % 2 != 0) {
            parentCount++;
        }

        // Select parents and re-populate parent array list
        for (int i = 0; i < parentCount; i++) { // optimize this by taking data structures out of the loop

            // Organize sub-population

            HashMap<Grid, Integer> subPop = new HashMap<>(); // Total sub-population of reproduction pool
            ArrayList<Map.Entry<Grid, Integer>> poolGrids = new ArrayList<>(reproductionPool.entrySet()); // Copy of reproduction pool entries

            // Place a random individual from the reproduction pool (copy) into sub-population
            // Remove that individual from reproduction pool copy so they are not selected twice
            for (int j = 0; j < populationSize / 2; j++) {
                int index = random.nextInt(poolGrids.size());
                subPop.put(poolGrids.get(index).getKey(), poolGrids.get(index).getValue());
                poolGrids.remove(index);
            }


            // Select parent from sub-population

            ArrayList<Map.Entry<Grid, Integer>> subPopGrids = new ArrayList<>(subPop.entrySet()); // Copy of sub-population entries
            subPopGrids.sort(Map.Entry.comparingByValue()); // Sorted to easily collect best score (index 0)

            if (!parents.contains(subPopGrids.get(0).getKey())) {
                parents.add(subPopGrids.get(0).getKey());
            } else {
                i--;
            }

        }
        return parents;

    }
}
