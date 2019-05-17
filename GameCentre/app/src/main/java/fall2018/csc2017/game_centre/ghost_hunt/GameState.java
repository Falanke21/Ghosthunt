package fall2018.csc2017.game_centre.ghost_hunt;

import java.io.Serializable;

import fall2018.csc2017.game_centre.GameTimer;

/**
 * Model
 *
 * Handle the game information board.
 */
class GameState implements Serializable {

    /**
     * Name for intent passing.
     */
    static String INTENT_NAME = "GhostHuntState";

    /**
     * Maximum levels of the game.
     */
    static int MAX_LEVEL = 3;

    /**
     * Board of the game.
     */
    private Board board;

    /**
     * The counter that counts the moves made by player.
     */
    private int moveCount;

    /**
     * Time the game has passed.
     */
    private GameTimer timer;

    /**
     * Construct a new state.
     * @param board level map
     */
    GameState(Board board) {
        this.board = board;
        this.moveCount = 0;
        this.timer = new GameTimer();
    }

    /**
     * Alternative constructor. For unit-testing purpose.
     * @param board board
     * @param timer timer
     */
    GameState(Board board, GameTimer timer) {
        this.board = board;
        this.moveCount = 0;
        this.timer = timer;
    }

    /**
     * Getter of the board.
     * @return the board
     */
    Board getBoard() {
        return this.board;
    }

    /**
     * Set the board for the state.
     * @param board new game board
     */
    void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Increment move count.
     */
    void incrementMoveCount() {
        this.moveCount += 1;
    }

    /**
     * Getter for move count.
     * @return move count
     */
    int getMoveCount() {
        return this.moveCount;
    }

    /**
     * Get timer in the state.
     * @return timer
     */
    GameTimer getTimer() {
        return this.timer;
    }
}

