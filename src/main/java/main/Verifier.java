package main;

import java.util.HashMap;
import java.util.Map;

import structure.Band;
import structure.Grid;
import structure.Region;
import structure.Square;
import structure.Stack;

/**
 * Inspects grid and deems whether it is a solved puzzle.
 * 
 * @author Adrian
 *
 */
public class Verifier {

	private Grid grid;
	private Map<Integer, Boolean> valueMap = new HashMap<>();

	/**
	 * Displays grid and runs deeper verification methods, before printing whether
	 * grid is solved.
	 */
	public void verify(Grid grid) {
		this.grid = grid;
		boolean validGrid = this.verifyBands() && this.verifyStacks() && this.verifyRegions();

		this.grid.displayGrid(validGrid);
	}

	/**
	 * Clears hash map for reuse.
	 */
	private void cleanMap() {
		for (int i = 1; i <= 9; i++) {
			valueMap.put(i, false);
		}
	}

	/**
	 * Ensures each band meets the criteria for a solved grid.
	 */
	private boolean verifyBands() {
		for (Band band : grid.getBandList()) {
			this.cleanMap();

			for (Square square : band.getSquareList()) {
				int value = square.getValue();

				if (valueMap.containsKey(value) && !valueMap.get(value)) {
					valueMap.put(value, true);
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Ensures each stack meets the criteria for a solved grid.
	 */
	private boolean verifyStacks() {
		for (Stack stack : grid.getStackList()) {
			this.cleanMap();

			for (Square square : stack.getSquareList()) {
				int value = square.getValue();

				if (valueMap.containsKey(value) && !valueMap.get(value)) {
					valueMap.put(value, true);
				} else {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Ensures each region meets the criteria for a solved grid.
	 */
	private boolean verifyRegions() {
		for (Region region : grid.getRegionList()) {
			this.cleanMap();

			for (Square square : region.getSquareList()) {
				int value = square.getValue();

				if (valueMap.containsKey(value) && !valueMap.get(value)) {
					valueMap.put(value, true);
				} else {
					return false;
				}
			}
		}
		return true;
	}
}