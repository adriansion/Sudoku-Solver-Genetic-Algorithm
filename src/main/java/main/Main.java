package main;

import solve.Genetic_Algorithm;
import structure.Grid;

/**
 * Main
 *
 * @author Adrian
 */
public class Main {

    public static void main(String[] args) {
        Log.logger.info("Starting application.");
        GridFileReader fileReader = new GridFileReader();
        Verifier verifier = new Verifier();
        Genetic_Algorithm GA = new Genetic_Algorithm();


        Log.logger.info("Reading and verifying grid.");
        Grid unsolved = fileReader.createGrid("Grid_Easy");
        verifier.verify(unsolved);

        Log.logger.info("Beginning solution.");

        Grid GASolve = GA.solve(unsolved);
        if (GASolve != null) {
            verifier.verify(GASolve);
            Log.logger.info("Solution complete.");
        }
    }
}