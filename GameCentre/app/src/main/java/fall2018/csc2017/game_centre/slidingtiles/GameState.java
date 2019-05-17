package fall2018.csc2017.game_centre.slidingtiles;

import java.io.Serializable;
import java.util.List;
import java.util.Stack;

/**
 * Model class, excluded from unit test(except the method save move).
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
class GameState implements Serializable {

    /**
     * The board being managed.
     */
    private Board board;

    /**
     * The counter that counts the moves made by player.
     */
    private int moveCounter = 0;

    /**
     * The maximum undo times that the user choose.
     */
    private final int MAX_UNDO;

    /**
     * The stack that is used for undo. Every move inside is made by an array of {row, col}
     * row and col both starts from 0.
     */
    private Stack<int[]> moveStack = new Stack<>();

    /**
     * Manage a new shuffled board.
     * @param maxUndo the maximum number of undo this game can have
     * @param boardLength the length of the board
     */
    GameState(int maxUndo, int boardLength) {
        SolvableGenerator solvableGenerator = new SolvableGenerator();
        List<Tile> tiles = solvableGenerator.generateTileList(boardLength);
        this.board = new Board(tiles, boardLength);
        this.MAX_UNDO = maxUndo;
    }

    /**
     * Constructor for testing usage.
     */
    GameState(int maxUndo, Board board) {
        this.board = board;
        this.MAX_UNDO = maxUndo;
    }

    /**
     * Return the current board.
     * @return the board of game.
     */
    Board getBoard() {
        return board;
    }

    /**
     * Return the moves that the player has played.
     * @return counter
     */
    int getMoveCounter() {
        return moveCounter;
    }

    /**
     * Increase the move counter by 1.
     */
    void increaseMoveCounter(){
        this.moveCounter++;
    }

    /**
     * Save the move into moveStack.
     * @param row the num of row of last blank tile, starts at 0
     * @param col the num of col of last blank tile, starts at 0
     */
    void saveMove(int row, int col){
        int[] move = {row, col};
        moveStack.push(move);
        if (moveStack.size() > MAX_UNDO){
            moveStack.remove(0);
        }
    }

    boolean isMoveStackEmpty(){
        return !moveStack.empty();
    }

    int[] getLastMove(){
        return moveStack.pop();
    }
}
