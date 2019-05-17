package fall2018.csc2017.game_centre.sudoku;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Model class, exclude from unit test.
 * Overall sudoku grid.
 */
class SudokuBoard implements Serializable {

    /**
     * The length of the board.
     */
    static final int SIDE_LEN = 9;

    /**
     * Entire 9*9 grid composed of 9 sub grids.
     */
    private Cell[][] grid;

    /**
     * Sudoku board constructor.
     *
     * @param emptyCells the number of empty cells that the player wants. Relates to difficulty.
     */
    SudokuBoard(int emptyCells) {
        grid = new Cell[SIDE_LEN][SIDE_LEN];
        SudokuGenerator sudokuGenerator = new SudokuGenerator();
        int[][] tempGrid = sudokuGenerator.generate(emptyCells);
        for (int i = 0; i < SIDE_LEN; i++) {
            for (int j = 0; j < SIDE_LEN; j++) {
                Cell newCell = new Cell(Math.abs(tempGrid[i][j]),
                        tempGrid[i][j] > 0, i * SIDE_LEN + j);
                grid[i][j] = newCell;
            }
        }
        setUpSameCells();
    }

    /**
     * This method adds a list of cells denotes the same value as the cell.
     */
    private void setUpSameCells() {
        for (int i = 1; i < 10; i++) {
            ArrayList<Cell> cells = new ArrayList<>();
            for (int j = 0; j < SIDE_LEN; j++) {
                for (int k = 0; k < SIDE_LEN; k++) {
                    if (grid[j][k].getValue() == i) {
                        cells.add(grid[j][k]);
                    }
                }
            }
            for (Cell cell : cells) {
                cell.setSameCells(cells);
            }
        }
    }

    /**
     * Getter for a cell.
     *
     * @param row the num of row
     * @param col the num of col
     * @return the cell of demand
     */
    Cell getCell(int row, int col) {
        return grid[row][col];
    }

    /**
     * Getter for side length
     *
     * @return the length of the board.
     */
    int getSideLen() {
        return SIDE_LEN;
    }
}
