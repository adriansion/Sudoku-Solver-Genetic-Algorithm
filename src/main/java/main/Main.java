package main;
import solve.Genetic_Algorithm;
import structure.Grid;

/**
 * Main
 * @author Adrian
 *
 */
public class Main {

	public static void main(String[] args) {
		Log.logger.info("Starting application.");
		GridFileReader fileReader = new GridFileReader();
		Verifier verifier = new Verifier();
		Genetic_Algorithm GA = new Genetic_Algorithm();



		Log.logger.info("Reading and verifying grid.");
		Grid unsolved = fileReader.createGrid("Grid_Anti-Backtracker");
		verifier.verify(unsolved);

		Log.logger.info("Solving grid.");

		Grid GASolve = GA.solve(unsolved);
		Log.logger.info("Grid solved.");
		verifier.verify(GASolve);


	}
}