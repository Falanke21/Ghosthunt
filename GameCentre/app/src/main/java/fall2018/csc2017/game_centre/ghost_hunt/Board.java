package fall2018.csc2017.game_centre.ghost_hunt;

import java.io.Serializable;

import fall2018.csc2017.game_centre.R;

/**
 * Model
 *
 * Board for ghost hunt. Contains information of the game.
 */
class Board implements Serializable {

    /**
     * Current level.
     */
    private int level;

    /**
     * The number of rows.
     */
    private int numRow;

    /**
     * The number of rows.
     */
    private int numCol;

    /**
     * The tiles on the grid.
     */
    private Tile[][] grid;

    /**
     * Player of the game.
     */
    private Player player;

    /**
     * Ghost of the game.
     */
    private Ghost ghost;

    /**
     * Initialize a board with level.
     * @param level level
     */
    Board(int level, Tile[][] grid, Player player, Ghost ghost) {
        this.level = level;
        this.grid = grid;
        this.numRow = grid.length;
        this.numCol = grid[0].length;
        this.player = player;
        this.ghost = ghost;
    }

    /**
     * Getter for level of the game.
     */
    int getLevel() {
        return level;
    }

    /**
     * Setter for level of the game. For unit-testing purpose.
     * @param level new level
     */
    void setLevel(int level) {
        this.level = level;
    }

    /**
     * Getter for number of rows.
     * @return number of rows
     */
    int getNumRow() {
        return numRow;
    }

    /**
     * Getter for number of columns.
     * @return number of columns
     */
    int getNumCol() {
        return numCol;
    }

    /**
     * Get the tile in the board located at row, col.
     * @param row row number
     * @param col column number
     * @return tile at row, col
     */
    Tile getTile(int row, int col) {
        return grid[row][col];
    }

    /**
     * Getter for player.
     * @return the player
     */
    Player getPlayer() {
        return this.player;
    }

    /**
     * Getter for ghost.
     * @return the ghost
     */
    Ghost getGhost() {
        return this.ghost;
    }

    /**
     * Return a tile's background in the board.
     * @param row row position
     * @param col column position
     * @return background resource
     */
    int getTileBackground(int row, int col) {
        if (player.getRow() == row && player.getCol() == col) {
            return player.getResource();
        } else if (ghost.getRow() == row && ghost.getCol() == col) {
            return ghost.getResource();
        } else {
            return R.drawable.empty_tile_ghost;
        }
    }
}
