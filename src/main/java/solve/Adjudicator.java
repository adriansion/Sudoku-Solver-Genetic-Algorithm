package solve;

import structure.Grid;
import structure.Square;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * When passed a grid, this class will deem its fitness and return a fitness score.
 *
 * @author Adrian
 */
public class Adjudicator {

    /**
     * Assigns a numerical fitness score to each grid passed.
     */
    public static int determineFitness(Grid grid) {
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

        return score;
    }
}
