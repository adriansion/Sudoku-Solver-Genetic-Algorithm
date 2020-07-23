package structure;

import java.util.ArrayList;

/**
 * This represents an individual column in the grid.
 *
 */
public class Stack {

	private ArrayList<Square> squareList = new ArrayList<Square>();

	public Stack(Grid grid, int stackNumber) {
		this.populateSquareList(grid, stackNumber);
		this.setAbovesAndBelows();
	}

	/**
	 * Sets stack number for appropriate squares in passed grid's square list, and
	 * fills stack's square list with each square.
	 * 
	 * stackNumber range will be [0,8]
	 */
	private void populateSquareList(Grid grid, int stackNumber) {
		for (int i = stackNumber; i <= 81 - (9 - stackNumber); i += 9) {
			grid.getSquareList().get(i).setStack(stackNumber);
			squareList.add(grid.getSquareList().get(i));
		}
	}

	/**
	 * Sets above adjacent and below adjacent squares to each square in stack's
	 * square list.
	 */
	private void setAbovesAndBelows() {
		for (int i = 0; i < 9; i++) {
			squareList.get(i).setAbove(i == 0 ? null : squareList.get(i - 1));
			squareList.get(i).setBelow(i == 8 ? null : squareList.get(i + 1));
		}
	}

	public ArrayList<Square> getSquareList() {
		return squareList;
	}
}