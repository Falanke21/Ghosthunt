package fall2018.csc2017.game_centre.sudoku;

import java.io.Serializable;
import java.util.List;

import fall2018.csc2017.game_centre.R;

/**
 * Model class, exclude from unit test.
 * Cell in sudoku board.
 */
class Cell implements Serializable {
    /**
     * The display background id.
     */
    private int background;

    /**
     * the uncolored blank background id of the cell.
     */
    private final int blankBackground = R.drawable.sudoku_blank;

    /**
     * the uncolored numbered background id of the cell.
     */
    private int numberBackground;

    /**
     * the colored numbered background id of the cell.
     */
    private int coloredBackground;

    /**
     * determine if the cell is visible, i.e. if the cell is displaying the number or a blank.
     */
    boolean isVisible;

    /**
     * The value of the cell, from 1-9
     */
    private final int value;

    /**
     * The position of the cell in the board.
     */
    private int position;

    /**
     * The List stores cells that have same value as this.
     */
    private List<Cell> sameCells;

    /**
     * Constructor of the cell.
     *
     * @param value     the preset value of the cell.
     * @param isVisible the visibility of the cell.
     * @param position  the position of the cell in the board.
     */
    Cell(int value, boolean isVisible, int position) {
        this.value = value;
        this.isVisible = isVisible;
        this.position = position;
        if (!isVisible) {
            background = blankBackground;
        } else {
            background = numberBackground;
        }
    }

    /**
     * Setter for List same cells
     *
     * @param cells the list of cells that have the same value as this.
     */
    void setSameCells(List<Cell> cells) {
        this.sameCells = cells;
    }

    /**
     * Setter for actual background id.
     *
     * @param background the id of background.
     */
    void setBackground(int background) {
        this.background = background;
    }

    /**
     * Setter for the number background, should be called from an Activity class.
     *
     * @param numberBackground the id of the number background.
     */
    void setNumberBackground(int numberBackground) {
        this.numberBackground = numberBackground;
        if (isVisible) {
            background = numberBackground;
        }
    }

    /**
     * Setter for the colored background, should be called from an Activity class.
     *
     * @param coloredBackground the id of the number background.
     */
    void setColoredBackground(int coloredBackground) {
        this.coloredBackground = coloredBackground;
    }

    /**
     * Change the background of the cell into colored according to it's visibility.
     */
    void colorCell() {
        if (isVisible) {
            background = coloredBackground;
        } else {
            background = R.drawable.sudoku_blank_coloured;
        }
    }

    /**
     * Decolor the background of the cell according to it's visibility.
     */
    void decolorCell() {
        if (isVisible) {
            background = numberBackground;
        } else {
            background = blankBackground;
        }
    }

    /**
     * Getter for the actual background.
     *
     * @return the background id.
     */
    int getBackground() {
        return background;
    }

    /**
     * Getter for the position of the cell.
     *
     * @return the position.
     */
    int getPosition() {
        return position;
    }

    /**
     * Getter for list same cells
     *
     * @return the list that contains the same value of cells.
     */
    List<Cell> getSameCells() {
        return sameCells;
    }

    /**
     * Getter for value.
     *
     * @return the value of the cell.
     */
    int getValue() {
        return value;
    }

    /**
     * Change the invisible cell into visible.
     */
    void changeToVisible() {
        this.isVisible = true;
        background = numberBackground;
    }

    /**
     * String representation of the cell.
     */
    public String toString() {
        if (isVisible) {
            return String.valueOf(value);
        } else {
            return String.valueOf(0);
        }
    }
}
