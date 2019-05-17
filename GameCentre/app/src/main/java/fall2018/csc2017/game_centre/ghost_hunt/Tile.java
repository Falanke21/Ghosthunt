package fall2018.csc2017.game_centre.ghost_hunt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model
 *
 * A tile in the board.
 */
class Tile implements Serializable {

    /**
     * If this tile is the exit.
     */
    private boolean isExit;

    /**
     * List of available moves in current tile.
     */
    private List<Direction> availableMoves;

    /**
     * Constructor taking in a list of available moves.
     * @param availableMoves available moves
     */
    Tile(ArrayList<Direction> availableMoves) {
        this.isExit = false;
        this.availableMoves = availableMoves;
    }

    /**
     * Set this tile to the exit.
     */
    void setExit() {
        this.isExit = true;
    }

    /**
     * Get if this tile is the exit.
     * @return if this tile is exit
     */
    boolean isExit() {
        return this.isExit;
    }

    /**
     * Return if move is valid under move
     * @return if possible
     */
    List<Direction> getAvailableMoves() {
        return availableMoves;
    }
}
