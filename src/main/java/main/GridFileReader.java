package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import structure.Grid;

/**
 * Reads numerical info from file; builds 9x9 grid containing all provided
 * values from file.
 *
 */
public class GridFileReader {

	private Grid grid;

	/**
	 * Performs reading and grid creation.
	 */
	public Grid createGrid(String filename) {
		grid = new Grid();
		try {
			File file = new File(filename);
			Scanner scanner = new Scanner(file);

			int counter = 0;

			while (scanner.hasNextLine() && counter < 81) {
				int line = Integer.parseInt(scanner.nextLine());
				grid.getSquareList().get(counter).setValue(line);
				counter++;
			}
			
			scanner.close();

		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
			e.printStackTrace();
		}

		grid.setName(filename);
		return grid;
	}
}
