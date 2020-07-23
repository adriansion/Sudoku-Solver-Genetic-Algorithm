package structure;

import java.util.ArrayList;

/**
 * This represents an individual row in the grid.
 *
 */
public class Band {

	private ArrayList<Square> squareList = new ArrayList<>();

	public Band(Grid grid, int bandNumber) {
		this.populateSquareList(grid, bandNumber);
		this.setLeftsAndRights();
	}

	/**
	 * Sets band number for appropriate squares in passed grid's square list, and
	 * fills band's square list with each square.
	 * 
	 * bandNumber range will be [0,8]
	 */
	private void populateSquareList(Grid grid, int bandNumber) {
		for (int i = bandNumber * 9; i < (bandNumber * 9) + 9; i++) {
			grid.getSquareList().get(i).setBand(bandNumber);
			squareList.add(grid.getSquareList().get(i));
		}
	}

	/**
	 * Sets left adjacent and right adjacent squares to each square in band's square
	 * list.
	 */
	private void setLeftsAndRights() {
		for (int i = 0; i < 9; i++) {
			squareList.get(i).setLeft(i == 0 ? null : squareList.get(i - 1));
			squareList.get(i).setRight(i == 8 ? null : squareList.get(i + 1));
		}
	}

	public ArrayList<Square> getSquareList() {
		return squareList;
	}
}