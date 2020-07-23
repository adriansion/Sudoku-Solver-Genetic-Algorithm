package structure;

import java.util.ArrayList;

/**
 * This represents one of nine sub-grids.
 *
 */
public class Region {

	private ArrayList<Square> squareList = new ArrayList<>();

	public Region(Grid grid, int regionNumber) {
		this.populateSquareList(grid, regionNumber);
	}

	/**
	 * Assigns each square in grid's square list an appropriate region number, and
	 * fills region's square list with appropriate squares in passed grid.
	 * 
	 * regionNumber range will be [0,8]
	 */
	private void populateSquareList(Grid grid, int regionNumber) {

		for (Square square : grid.getSquareList()) {
			square.setRegion((square.getStack() / 3) + (3 * (square.getBand() / 3)));

			if (square.getRegion() == regionNumber) {
				squareList.add(square);
			}
		}
	}

	public ArrayList<Square> getSquareList() {
		return squareList;
	}
}