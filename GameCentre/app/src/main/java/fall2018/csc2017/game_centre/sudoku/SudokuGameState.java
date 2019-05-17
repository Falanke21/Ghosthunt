package fall2018.csc2017.game_centre.sudoku;

import java.io.Serializable;
import java.util.Observable;

import fall2018.csc2017.game_centre.GameTimer;

/**
 * Model class, exclude from unit test.
 * Game state of sudoku. Contains sudoku board, hint counter, game time etc.
 */
class SudokuGameState extends Observable implements Serializable {
    /**
     * Board of sudoku.
     */
    private SudokuBoard board;

    /**
     * Difficulty of the Game.
     */
    private String difficulty;

    /**
     * The counter of remaining hints. Set to 3 initial.
     */
    private int hintCounter = 3;

    /**
     * The counter of wrongAttempts. If wrong answer reaches 4 than game is over.
     */
    private int wrongCounter;

    /**
     * The timer for the game.
     */
    private GameTimer timer;

    /**
     * Constructor of Sudoku Game state. It sets up the sudoku board and a timer.
     * @param emptyCells The number of emptyCells needed in board. Associated with difficulty.
     */
    SudokuGameState(int emptyCells, String difficulty){
        this.board = new SudokuBoard(emptyCells);
        this.difficulty = difficulty;
        timer = new GameTimer();
    }

    /**
     * Getter for game timer.
     * @return the timer of the game.
     */
    GameTimer getTimer() {
        return timer;
    }

    /**
     * Getter for board.
     * @return the sudoku board.
     */
    SudokuBoard getBoard(){
        return this.board;
    }

    /**
     * Getter for the wrong times.
     * @return the number of times that the user got wrong.
     */
    int getWrongCounter() {
        return wrongCounter;
    }

    /**
     * Increase the wrong counter by 1. If the counter reaches 4, notify the gameActivity that
     * game is over.
     */
    void increaseWrongCounter(){
        wrongCounter++;
        if (wrongCounter == 4){
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Decrease the hint counter by 1.
     */
    void decreaseHintCounter(){
        hintCounter--;
    }

    /**
     * The total time played.
     * @return the total time calculated by the game timer.
     */
    int getTotalTime(){
        return timer.getTotalTime();
    }

    /**
     * Getter for the hint counter.
     * @return the number of hints remaining.
     */
    int getHintCounter() {
        return hintCounter;
    }

    /**
     * @return The difficulty of the current game.
     */
    String getDifficulty() {
        return difficulty;
    }
}
