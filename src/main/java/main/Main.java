package main;

import solve.Executor;
import solve.Genetic_Algorithm;
import structure.Grid;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main
 *
 * @author Adrian
 */
public class Main {

    public static void main(String[] args) {

        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 1; i++){
            pool.execute(new Executor());
        }
        pool.shutdown();
    }
}