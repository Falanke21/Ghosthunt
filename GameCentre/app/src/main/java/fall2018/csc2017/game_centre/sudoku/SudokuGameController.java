package fall2018.csc2017.game_centre.sudoku;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * The controller that actually controls the state of the game.
 */
class SudokuGameController extends Observable {

    /**
     * The sudokuBoard board of the game.
     */
    private SudokuBoard board;

    /**
     * The Game state of sudoku.
     */
    private SudokuGameState gameState;

    /**
     * The file handler of the game. Mainly uses for auto save.
     */
    private SudokuFileHandler fileHandler = SudokuFileHandler.getInstance();

    /**
     * A Cell that denotes a blankSelected cell. Value of null if no cell is blankSelected.
     */
    private Cell blankSelected;

    /**
     * A list of colored cells at present. Supposed to be used for decoloring.
     */
    private List<Cell> coloredCells = new ArrayList<>();

    /**
     * The constructor. Nothing else.
     */
    SudokuGameController() {
    }

    /**
     * Setter for game state.
     *
     * @param gameState the boardManager to be set
     */
    void setGameState(SudokuGameState gameState) {
        this.gameState = gameState;
        this.board = gameState.getBoard();
    }

    /**
     * Handle the answer button clicked event.
     * <p>
     * Algorithm: if there is no cell blankSelected, do nothing.
     * if blankSelected value is different from answer button value, prompt wrong and
     * increase wrong counter.
     * else change blankSelected to visible, and change the background of the cell.
     *
     * @param context   the context instance needed for Toast.
     * @param buttonNum the number appear on the answer button.
     */
    void answerButtonClicked(Context context, int buttonNum) {
        if (blankSelected != null && !blankSelected.isVisible) {
            if (blankSelected.getValue() != buttonNum) {
                setChanged();
                notifyObservers("Wrong Answer");
                gameState.increaseWrongCounter();

            } else {
                answerIsRight();
                checkPuzzleSolved();
            }
            fileHandler.saveToFile(context);
        }
    }

    /**
     * Check if the puzzle is solved. if so, notify the activity.
     */
    void checkPuzzleSolved(){
        if (puzzleSolved()) {
            // Game Finished
            setChanged();
            notifyObservers(new int[]{gameState.getTotalTime(),
                    gameState.getHintCounter(), gameState.getWrongCounter()});
            fileHandler.setGameState(null);
        }
    }

    /**
     * This method gets called when the answer button gets clicked and is a right answer.
     */
    private void answerIsRight() {
        blankSelected.changeToVisible();

        // Display
        coloredCells = new ArrayList<>();
        coloredCells.add(blankSelected);
        setChanged();
        notifyObservers(coloredCells);
        blankSelected = null;
    }

    /**
     * Check if the puzzle is solved by checking if the cells are all visible.
     *
     * @return true if and only if puzzle is solved.
     */
    boolean puzzleSolved() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!board.getCell(i, j).isVisible) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * process tap on the board. decolor the color of all colored cells.
     *
     * @param position the position that the player chooses
     */
    void processTapMovement(int position) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board.getCell(i, j).decolorCell();
            }
        }

        touchCell(position);
    }

    /**
     * Handle the situation that a cell in grid is touched.
     * <p>
     * Algorithm: if the cell is visible, color all visible cells that have the same value.
     * also set blankSelected to be null.
     * if the cell is not visible, set blankSelected to be cell.
     * <p>
     * coloredCells contains all cells that need to change it's color.
     *
     * @param position the position that player touches.
     */
    private void touchCell(int position) {
        int row = position / SudokuBoard.SIDE_LEN;
        int col = position % SudokuBoard.SIDE_LEN;
        Cell cell = board.getCell(row, col);
        ArrayList<Cell> cellsNeedToChange = new ArrayList<>(coloredCells);
        if (cell.isVisible) {
            touchVisibleCell(cell);
            cellsNeedToChange.addAll(cell.getSameCells());
        } else {
            touchInvisibleCell(cell);
            cellsNeedToChange.add(cell);
        }
        // Display
        setChanged();
        notifyObservers(cellsNeedToChange);
    }

    /**
     * color all visible cells that have the same value.
     * also set blankSelected to be null.
     *
     * @param cell the cell that was touched.
     */
    private void touchVisibleCell(Cell cell) {
        blankSelected = null;
        for (Cell sameValueCell : cell.getSameCells()) {
            if (sameValueCell.isVisible) {
                sameValueCell.colorCell();
            }
        }
        coloredCells = cell.getSameCells();
    }

    /**
     * if the cell is not visible, set blankSelected to be cell.
     *
     * @param cell the cell that was touched.
     */
    private void touchInvisibleCell(Cell cell) {
        blankSelected = cell;
        cell.colorCell();
        coloredCells = new ArrayList<>();
        coloredCells.add(cell);
    }

    /**
     * Apply hint on the game, if there is a selected cell and hint counter still positive.
     *
     * @param context the context needed for Toast.
     */
    void hint(Context context) {
        if (gameState.getHintCounter() <= 0) {
            setChanged();
            notifyObservers("No Hint Remains");
        } else {
            if (blankSelected == null) {
                setChanged();
                notifyObservers("Please Select A Empty Cell");
            } else {
                applyHint(context);
                checkPuzzleSolved();
                }
            }
        }

    /**
     * Apply the hint on the board.
     * @param context the context needed for file io.
     */
    private void applyHint(Context context){
        blankSelected.changeToVisible();
        blankSelected = null;
        gameState.decreaseHintCounter();
        fileHandler.saveToFile(context);
        setChanged();
        notifyObservers();
    }
}
