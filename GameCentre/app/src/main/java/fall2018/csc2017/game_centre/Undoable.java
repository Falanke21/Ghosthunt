package fall2018.csc2017.game_centre;

/**
 * This Undoable interface is designed for games that contains an undo feature.
 */
public interface Undoable {
    /**
     * Performs a move undo on the game.
     */
    void undo();
}
