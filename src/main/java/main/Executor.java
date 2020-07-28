package main;

import main.GridFileReader;
import main.Log;
import main.Verifier;
import solve.Genetic_Algorithm;
import structure.Grid;

public class Executor implements Runnable {

    @Override
    public void run() {
//        Log.logger.info("Starting application.");
        GridFileReader fileReader = new GridFileReader();
        Verifier verifier = new Verifier();
        Genetic_Algorithm GA = new Genetic_Algorithm();


//        Log.logger.info("Reading and verifying grid.");
        Grid unsolved = fileReader.createGrid("Grid_OneSquareMissing");
//        verifier.verify(unsolved);



//        Log.logger.info("Beginning solution.");

        Grid GASolve = GA.solve(unsolved, 0);
        if (GASolve != null) {
            verifier.verify(GASolve);
            Log.logger.info("Solution complete.");
        }
//        System.out.println("Done");

    }
}
