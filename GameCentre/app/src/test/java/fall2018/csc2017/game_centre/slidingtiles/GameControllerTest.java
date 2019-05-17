package fall2018.csc2017.game_centre.slidingtiles;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameControllerTest {

    private GameController testController;
    private GameState gameState;
    private Context context;


    /**
     * Make a set of tiles that are in order.
     *
     * @param size size of the grid
     * @return a set of tiles that are in order
     */
    private List<Tile> makeOrderedTiles(int size) {
        List<Tile> tiles = new ArrayList<>();
        final int numTiles = size * size;
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum));
        }
        return tiles;
    }

    /**
     * @param  array an array of integers
     * @return a list of tiles that are ordered as array.
     */
    private List<Tile> createTileList(int[] array) {
        List<Tile> tiles = new ArrayList<>();
        for (int i : array) {
            tiles.add(new Tile(i));
        }
        return tiles;
    }

    @Before
    public void setUp() {
        testController= new GameController();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testProcessTapMovementOneMove() {
        List<Tile> tiles = makeOrderedTiles(3);
        Board board = new Board(tiles, 3);
        gameState = new GameState(1, board);
        testController.setGameState(gameState);
        assertEquals(9, gameState.getBoard().getTile(2, 2).getId());
        assertEquals(6, gameState.getBoard().getTile(1, 2).getId());
        testController.processTapMovement(context, 5);
        assertEquals(6, gameState.getBoard().getTile(2, 2).getId());
        assertEquals(9, gameState.getBoard().getTile(1, 2).getId());
    }

    @Test
    public void testProcessTapMovementPuzzleSolved() {
        Board board = new Board(createTileList(new int[]{0, 1, 2, 3, 4, 5, 6, 8, 7}), 3);
        gameState = new GameState(2, board);
        testController.setGameState(gameState);
        testController.processTapMovement(context, 8);
        assertTrue(testController.puzzleSolved());
    }

    @Test
    public void testProcessTapMovementInvalidTap() {
        Board board = new Board(createTileList(new int[]{0, 1, 2, 3, 4, 5, 6, 8, 7}), 3);
        gameState = new GameState(2, board);
        testController.setGameState(gameState);
        testController.processTapMovement(context, 0);
        assertEquals(board.getTile(2, 2).getId(), 8);
        assertFalse(testController.puzzleSolved());
    }

    @Test
    public void testProcessTapMovementPuzzleNotSolved() {
        Board board = new Board(createTileList(new int[]{0, 1, 2, 5, 4, 3, 6, 7, 8}), 3);
        gameState = new GameState(2, board);
        testController.setGameState(gameState);
        testController.processTapMovement(context, 0);
        assertFalse(testController.puzzleSolved());
    }

    @Test
    public void undoAbove() {
        List<Tile> tiles = makeOrderedTiles(3);
        Board board = new Board(tiles, 3);
        gameState = new GameState(1, board);
        testController.setGameState(gameState);
        assertEquals(9, gameState.getBoard().getTile(2, 2).getId());
        assertEquals(6, gameState.getBoard().getTile(1, 2).getId());
        testController.processTapMovement(context, 5);
        assertEquals(6, gameState.getBoard().getTile(2, 2).getId());
        assertEquals(9, gameState.getBoard().getTile(1, 2).getId());
        testController.undo();
        assertEquals(9, gameState.getBoard().getTile(2, 2).getId());
        assertEquals(6, gameState.getBoard().getTile(1, 2).getId());
    }

    @Test
    public void testUndoFourDirection() {
        Board board = new Board(createTileList(new int[]{0, 1, 2, 3, 8, 5, 6, 7, 4}), 3);
        gameState = new GameState(2, board);
        testController.setGameState(gameState);
        testController.processTapMovement(context, 1);
        testController.undo();
        testController.processTapMovement(context, 3);
        testController.undo();
        testController.processTapMovement(context, 5);
        testController.undo();
        testController.processTapMovement(context, 7);
        testController.undo();
        Board boardToCompare = new Board(createTileList(new int[]{0, 1, 2, 3, 8, 5, 6, 7, 4}), 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(board.getTile(i, j).getId(), boardToCompare.getTile(i, j).getId());
            }
        }
    }
}