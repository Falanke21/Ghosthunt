/**
 * This is a template of tests written by Chris.
 */
//package fall2018.csc2017.game_centre.slidingtiles;
//
//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//
///**
// * Test class for GameState.
// */
//public class TestBoardManager {
//
//    /**
//     * Default value for undo.
//     */
//    private static final int DEFAULT_UNDO = 5;
//
//    /**
//     * Sizes of the grid.
//     */
//    private static final int EASY_SIZE = 3;
//    private static final int MEDIUM_SIZE = 4;
//    private static final int HARD_SIZE = 5;
//
//    /**
//     * Board manager for testing.
//     */
//    private GameState boardManager;
//
//    /**
//     * Make a set of tiles that are in order.
//     *
//     * @param size size of the grid
//     * @return a set of tiles that are in order
//     */
//    private List<Tile> makeOrderedTiles(int size) {
//        List<Tile> tiles = new ArrayList<>();
//        final int numTiles = size * size;
//        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
//            tiles.add(new Tile(tileNum));
//        }
//        return tiles;
//    }
//
//    /**
//     * Make a solved Board.
//     *
//     * @param size size of the grid
//     */
//    private void setUpCorrect(int size) {
//        List<Tile> tiles = makeOrderedTiles(size);
//        Board board = new Board(tiles, size);
//        boardManager = new GameState(DEFAULT_UNDO, board);
//    }
//
//    /**
//     * Test whether swapping two tiles makes a solved board unsolved. For easy size.
//     */
//    @Test
//    public void testIsSolvedEasy() {
//        setUpCorrect(EASY_SIZE);
//        assertTrue(boardManager.puzzleSolved());
//        boardManager.getBoard().swapTiles(0, 0, 0, 1);
//        assertFalse(boardManager.puzzleSolved());
//    }
//
//    /**
//     * Test whether swapping two tiles makes a solved board unsolved. For medium size.
//     */
//    @Test
//    public void testIsSolvedMedium() {
//        setUpCorrect(MEDIUM_SIZE);
//        assertTrue(boardManager.puzzleSolved());
//        boardManager.getBoard().swapTiles(0, 0, 0, 1);
//        assertFalse(boardManager.puzzleSolved());
//    }
//
//    /**
//     * Test whether swapping two tiles makes a solved board unsolved. For hard size.
//     */
//    @Test
//    public void testIsSolvedHard() {
//        setUpCorrect(HARD_SIZE);
//        assertTrue(boardManager.puzzleSolved());
//        boardManager.getBoard().swapTiles(0, 0, 0, 1);
//        assertFalse(boardManager.puzzleSolved());
//    }
//
//    /**
//     * Test whether swapping the first two tiles works. For easy size.
//     */
//    @Test
//    public void testSwapFirstTwoEasy() {
//        setUpCorrect(EASY_SIZE);
//        assertEquals(1, boardManager.getBoard().getTile(0, 0).getId());
//        assertEquals(2, boardManager.getBoard().getTile(0, 1).getId());
//        boardManager.getBoard().swapTiles(0, 0, 0, 1);
//        assertEquals(2, boardManager.getBoard().getTile(0, 0).getId());
//        assertEquals(1, boardManager.getBoard().getTile(0, 1).getId());
//    }
//
//    /**
//     * Test whether swapping the first two tiles works. For medium size.
//     */
//    @Test
//    public void testSwapFirstTwoMedium() {
//        setUpCorrect(MEDIUM_SIZE);
//        assertEquals(1, boardManager.getBoard().getTile(0, 0).getId());
//        assertEquals(2, boardManager.getBoard().getTile(0, 1).getId());
//        boardManager.getBoard().swapTiles(0, 0, 0, 1);
//        assertEquals(2, boardManager.getBoard().getTile(0, 0).getId());
//        assertEquals(1, boardManager.getBoard().getTile(0, 1).getId());
//    }
//
//    /**
//     * Test whether swapping the first two tiles works. For hard size.
//     */
//    @Test
//    public void testSwapFirstTwoHard() {
//        setUpCorrect(HARD_SIZE);
//        assertEquals(1, boardManager.getBoard().getTile(0, 0).getId());
//        assertEquals(2, boardManager.getBoard().getTile(0, 1).getId());
//        boardManager.getBoard().swapTiles(0, 0, 0, 1);
//        assertEquals(2, boardManager.getBoard().getTile(0, 0).getId());
//        assertEquals(1, boardManager.getBoard().getTile(0, 1).getId());
//    }
//
//    /**
//     * Test whether swapping the last two tiles works. For easy size.
//     */
//    @Test
//    public void testSwapLastTwoEasy() {
//        setUpCorrect(EASY_SIZE);
//        assertEquals(8, boardManager.getBoard().getTile(2, 1).getId());
//        assertEquals(9, boardManager.getBoard().getTile(2, 2).getId());
//        boardManager.getBoard().swapTiles(2, 1, 2, 2);
//        assertEquals(9, boardManager.getBoard().getTile(2, 1).getId());
//        assertEquals(8, boardManager.getBoard().getTile(2, 2).getId());
//    }
//
//    /**
//     * Test whether swapping the last two tiles works. For medium size.
//     */
//    @Test
//    public void testSwapLastTwoMedium() {
//        setUpCorrect(MEDIUM_SIZE);
//        assertEquals(15, boardManager.getBoard().getTile(3, 2).getId());
//        assertEquals(16, boardManager.getBoard().getTile(3, 3).getId());
//        boardManager.getBoard().swapTiles(3, 3, 3, 2);
//        assertEquals(16, boardManager.getBoard().getTile(3, 2).getId());
//        assertEquals(15, boardManager.getBoard().getTile(3, 3).getId());
//    }
//
//    /**
//     * Test whether swapping the last two tiles works. For hard size.
//     */
//    @Test
//    public void testSwapLastTwoHard() {
//        setUpCorrect(HARD_SIZE);
//        assertEquals(24, boardManager.getBoard().getTile(4, 3).getId());
//        assertEquals(25, boardManager.getBoard().getTile(4, 4).getId());
//        boardManager.getBoard().swapTiles(4, 3, 4, 4);
//        assertEquals(25, boardManager.getBoard().getTile(4, 3).getId());
//        assertEquals(24, boardManager.getBoard().getTile(4, 4).getId());
//    }
//
//    /**
//     * Test whether isValidHelp works. For easy size.
//     */
//    @Test
//    public void testIsValidTapEasy() {
//        setUpCorrect(EASY_SIZE);
//        assertTrue(boardManager.isValidTap(5));
//        assertTrue(boardManager.isValidTap(7));
//        assertFalse(boardManager.isValidTap(4));
//    }
//
//    /**
//     * Test whether isValidHelp works. For medium size.
//     */
//    @Test
//    public void testIsValidTapMedium() {
//        setUpCorrect(MEDIUM_SIZE);
//        assertTrue(boardManager.isValidTap(11));
//        assertTrue(boardManager.isValidTap(14));
//        assertFalse(boardManager.isValidTap(10));
//    }
//
//    /**
//     * Test whether isValidHelp works. For hard size.
//     */
//    @Test
//    public void testIsValidTapHard() {
//        setUpCorrect(HARD_SIZE);
//        assertTrue(boardManager.isValidTap(19));
//        assertTrue(boardManager.isValidTap(23));
//        assertFalse(boardManager.isValidTap(18));
//    }
//}
