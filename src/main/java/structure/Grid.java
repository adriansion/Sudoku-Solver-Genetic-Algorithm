package structure;

import java.util.ArrayList;

/**
 * This represents the 9x9 board
 */
public class Grid {

    private String gridName;

    private ArrayList<Square> squareList = new ArrayList<>();
    private ArrayList<Band> bandList = new ArrayList<>();
    private ArrayList<Stack> stackList = new ArrayList<>();
    private ArrayList<Region> regionList = new ArrayList<>();

    private int generation;

    public Grid() {
        this.populateSquareList();

        for (int i = 0; i < 9; i++) {
            bandList.add(new Band(this, i));
            stackList.add(new Stack(this, i));
        }

        for (int i = 0; i < 9; i++) {
            regionList.add(new Region(this, i));
        }

    }

    public void setName(String name) {
        gridName = name;
    }

    public String getName() {
        return gridName;
    }

    /**
     * Fills grid's square list with 81 distinct squares.
     */
    private void populateSquareList() {
        for (int i = 0; i < 81; i++) {
            Square square = new Square();
            square.setGridSpot(i);
            squareList.add(square);
        }
    }

    public ArrayList<Square> getSquareList() {
        return squareList;
    }

    public ArrayList<Band> getBandList() {
        return bandList;
    }

    public ArrayList<Stack> getStackList() {
        return stackList;
    }

    public ArrayList<Region> getRegionList() {
        return regionList;
    }

    /**
     * Prints 9x9 representation of grid, containing each of its 81 squares.
     */
    public void displayGrid(boolean v) {
        for (Band band : bandList) {
            band.getSquareList().forEach((n) -> System.out.print((n.getValue() == -1 ? "" : " ") + n.getValue() + " "));
            System.out.println();
        }
        System.out.println("\n" + gridName + ": " + (v ? "Valid" : "Invalid") + " solution.\n" + "_".repeat(30) + "\n");
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public int getGeneration() {
        return this.generation;
    }
}