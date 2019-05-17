package fall2018.csc2017.game_centre.slidingtiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The class is aiming for generating sliding tile games that are solvable
 */
class SolvableGenerator {

    /**
     * Generate a list of tiles that are guaranteed to be solvable.
     * @param boardLength the length of the board
     * @return the list of solvable tiles in order
     */
    List<Tile> generateTileList(int boardLength){
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = boardLength * boardLength;

        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        do {
            Collections.shuffle(tiles);
        } while (!isSolvable(tiles, boardLength));

        return tiles;
    }

    /**
     * Check if the board is solvable. Adapted from
     * http://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html
     *
     * @param tiles array of tiles to check
     * @param length size of the grid
     * @return if the board is solvable
     */
    boolean isSolvable(List<Tile> tiles, int length) {
        int inversion = countInversion(tiles);
        if (length % 2 == 1) {
            return inversion % 2 == 0;
        } else {
            int blankRow = countBlankRowFromBottom(tiles, length);
            return (blankRow % 2 == 1) == (inversion % 2 == 0);
        }
    }

    /**
     * Count the amount of inversions in the board.
     *
     * @param tiles array of tiles to check
     * @return amount of inversions
     */
    private int countInversion(List<Tile> tiles) {
        int counter = 0;
        int length = tiles.size();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < i; j++) {
                int jId = tiles.get(j).id;
                int iId = tiles.get(i).id;
                if (jId > iId && jId != length && iId != length) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Get the blank tile's row counting from bottom.
     *
     * @param tiles array of tiles to check
     * @param length size of the grid
     * @return blank tile's row
     */
    private int countBlankRowFromBottom(List<Tile> tiles, int length) {
        int res = 0;
        boolean notFound = true;
        int i = tiles.size() - 1;
        while (notFound && i >= 0) {
            if (tiles.get(i).id == length * length) {
                res = (tiles.size() - i) / length + 1;
                notFound = false;
            }
            i--;
        }
        return res;
    }
}
