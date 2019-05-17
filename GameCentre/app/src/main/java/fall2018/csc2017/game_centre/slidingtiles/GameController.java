package fall2018.csc2017.game_centre.slidingtiles;

import android.content.Context;

import java.util.Iterator;
import java.util.Observable;

import fall2018.csc2017.game_centre.Undoable;

/**
 * The game controller of game sliding tiles.
 */
class GameController extends Observable implements Undoable {

    /**
     * Current board manager.
     */
    private GameState gameState = null;

    /**
     * Current board of the game.
     */
    private Board board;

    /**
     * The file saver for game.
     */
    private SlidingTilesFileHandler fileSaver;

    /**
     * Constructor.
     */
    GameController() {
        fileSaver = SlidingTilesFileHandler.getInstance();
    }

    /**
     * Setter for gameState
     *
     * @param gameState the gameState to be set
     */
    void setGameState(GameState gameState) {
        this.gameState = gameState;
        this.board = gameState.getBoard();
    }

    /**
     * Check if the move is valid, if yes, process the move. Also checks whether the game has ended
     * or not. Also uses the counter to auto save the game.
     *
     * @param context  context, suppose to be the gameActivity, used for file io.
     * @param position the position that the player chooses
     */
    void processTapMovement(Context context, int position) {
        if (isValidTap(position)) {
            touchMove(position);

            if (gameState.getMoveCounter() % 5 == 0) {
                fileSaver.saveToFile(context);
            }

            if (puzzleSolved()) {
                setChanged();
                notifyObservers(gameState.getMoveCounter());
            }
        } else {
            setChanged();
            notifyObservers("Invalid Tap");
        }
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    boolean puzzleSolved() {
        Tile lastElement = board.getTile(board.getLength() - 1, board.getLength() - 1);
        if (lastElement.getId() != board.numTiles()) {
            return false;
        }
        Iterator<Tile> bIterator = this.board.iterator();
        for (int i = 0; i < board.numTiles(); i++) {
            Tile temp = bIterator.next();
            if (temp.getId() != i + 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    private void touchMove(int position) {
        gameState.increaseMoveCounter();
        int row = position / board.getLength();
        int col = position % board.getLength();
        int blankId = board.numTiles();
        Tile[] nearbyTiles = generateNearbyTile(row, col);
        Tile above = nearbyTiles[0], below = nearbyTiles[1],
                left = nearbyTiles[2], right = nearbyTiles[3];
        if (above != null && above.getId() == blankId) {
            this.board.swapTiles(row, col, row - 1, col);
            gameState.saveMove(row - 1, col);
        }
        if (below != null && below.getId() == blankId) {
            this.board.swapTiles(row, col, row + 1, col);
            gameState.saveMove(row + 1, col);
        }
        if (left != null && left.getId() == blankId) {
            this.board.swapTiles(row, col, row, col - 1);
            gameState.saveMove(row, col - 1);
        }
        if (right != null && right.getId() == blankId) {
            this.board.swapTiles(row, col, row, col + 1);
            gameState.saveMove(row, col + 1);
        }
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    private boolean isValidTap(int position) {
        int row = position / board.getLength();
        int col = position % board.getLength();
        int blankId = board.numTiles();
        Tile[] nearbyTiles = generateNearbyTile(row, col);
        Tile above = nearbyTiles[0], below = nearbyTiles[1], left = nearbyTiles[2], right = nearbyTiles[3];
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Returns a array of tiles which are above, below, left and right tiles of
     * the given tile at row, col
     *
     * @param row the # of row for the given tile
     * @param col the # of col for the given tile
     * @return an array of tiles
     */
    private Tile[] generateNearbyTile(int row, int col) {
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == board.getLength() - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == board.getLength() - 1 ? null : board.getTile(row, col + 1);

        return new Tile[]{above, below, left, right};
    }

    /**
     * Performs a move undo on the board.
     */
    @Override
    public void undo() {
        if (gameState.isMoveStackEmpty()) {
            int blankId = board.numTiles();
            int[] move = gameState.getLastMove();
            Tile[] nearbyTiles = generateNearbyTile(move[0], move[1]);
            Tile above = nearbyTiles[0], below = nearbyTiles[1],
                    left = nearbyTiles[2], right = nearbyTiles[3];

            if (above != null && above.getId() == blankId)
                this.board.swapTiles(move[0], move[1], move[0] - 1, move[1]);
            if (below != null && below.getId() == blankId)
                this.board.swapTiles(move[0], move[1], move[0] + 1, move[1]);
            if (left != null && left.getId() == blankId)
                this.board.swapTiles(move[0], move[1], move[0], move[1] - 1);
            if (right != null && right.getId() == blankId)
                this.board.swapTiles(move[0], move[1], move[0], move[1] + 1);
        }
    }
}
