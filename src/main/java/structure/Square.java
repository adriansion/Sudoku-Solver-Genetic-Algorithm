package structure;

import java.util.ArrayList;

/**
 * This represents each of the 81 individual grid squares
 *
 * @author Adrian
 */
public class Square {

	// Square identification
	private int value, band, stack, region, gridSpot;
	private boolean isPreSolved;

	// Linked-style adjacent squares
	private Square left, right, above, below;

	// Meant to contain possible values while solving grid
	private ArrayList<String> candidates = new ArrayList<>();

	// Constructors
	public Square(int value) {
		this.setValue(value);
	}

	public Square() {

	}

	// Square identification

	// Value
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	// Band
	public int getBand() {
		return band;
	}

	public void setBand(int band) {
		this.band = band;
	}

	// Stack
	public int getStack() {
		return stack;
	}

	public void setStack(int stack) {
		this.stack = stack;
	}

	// Region
	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	// Grid spot
	public int getGridSpot() {
		return gridSpot;
	}

	public void setGridSpot(int gridSpot) {
		this.gridSpot = gridSpot;
	}

	// Clue
	public boolean isPreSolved() {
		return isPreSolved;
	}

	public void setPreSolved(boolean preSolved) {
		this.isPreSolved = preSolved;
	}

	// Linked-style adjacent squares

	public Square getLeft() {
		return left;
	}

	public void setLeft(Square left) {
		this.left = left;
	}

	public Square getRight() {
		return right;
	}

	public void setRight(Square right) {
		this.right = right;
	}

	public Square getAbove() {
		return above;
	}

	public void setAbove(Square above) {
		this.above = above;
	}

	public Square getBelow() {
		return below;
	}

	public void setBelow(Square below) {
		this.below = below;
	}

	// Candidates: Meant to contain possible values while solving grid
	// USE CANDIDATES FOR BACKTRACKING ALGORITHM

	public ArrayList<Integer> getCandidates() {
		ArrayList<Integer> intList = new ArrayList<>();
		
		for (String candidate : candidates) {
			intList.add(Integer.parseInt(candidate));
		}
		return intList;
	}

	public void addCandidate(int candidate) {
		candidates.add(Integer.toString(candidate));
		candidates.sort(null);
	}

	public void removeCandidate(int candidate) {
		candidates.remove(Integer.toString(candidate));
		candidates.sort(null);
	}

}
