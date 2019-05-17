package fall2018.csc2017.game_centre.ghost_hunt;

import android.content.Context;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Stack;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class GameControllerTest {

    /**
     * Mock board.
     */
    @Mock
    private Board mockBoard;

    /**
     * Mock state.
     */
    @Mock
    private GameState mockState;

    /**
     * Mock context.
     */
    @Mock
    private Context mockContext;

    /**
     * Mock file handler.
     */
    @Mock
    private FileHandler mockHandler;

    /**
     * Mock observer, or mock game activity.
     */
    @Mock
    private GhostHuntGameActivity mockObserver;

    /**
     * Mockito mocking rule.
     */
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    /**
     * Controller to be tested with.
     */
    private GameController controller;

    /**
     * Mocking a controller with mocking context, file handler and state.
     */
    private void setMockController() {
        controller = new GameController(mockContext, mockHandler, mockState);
    }

    /**
     * Set up a game controller with simple board state,
     */
    private void setSimpleStateController() {
        GameState state = new GameState(makeSimpleBoard());
        controller = new GameController(mockContext, mockHandler, state);
    }

    /**
     * Make a simple ghost hunt board.
     * p = player
     * g = ghost
     * E = exit
     * +---+---+---+
     * |         g |
     * +   +   +   +
     * | E         |
     * +   +   +   +
     * |     p     |
     * +---+---+---+
     * @return above board
     */
    private Board makeSimpleBoard() {
        FileHandler handler = FileHandler.getInstance();
        String[] map_csv = {
                "0110,0111,0011",
                "1110,1111,1011",
                "1100,1101,1001"
        };
        int row = 0;
        Tile[][] grid = new Tile[3][3];
        for (String line : map_csv) {
            grid[row] = handler.readTile(line);
            row++;
        }
        grid[1][0].setExit();
        Player p = new Player(2, 1);
        Ghost g = new Ghost(0, 2);
        return new Board(1, grid, p, g);
    }

    @After
    public void tearDown() {
        controller = null;
    }

    @Test
    public void constructor() {
        controller = new GameController(mockContext, mockState);
        assertEquals(mockState, FileHandler.getInstance().getState());
    }

    @Test
    public void setContext() {
        setMockController();
        Context context = mock(Context.class);
        controller.setContext(context);
        assertEquals(context, controller.getContext());
    }

    @Test
    public void getContext() {
        setMockController();
        Context context = mock(Context.class);
        controller.setContext(context);
        assertEquals(context, controller.getContext());
    }

    @Test
    public void setState() {
        setMockController();
        GameState state = mock(GameState.class);
        controller.setState(state);
        assertEquals(state, controller.getState());
    }

    @Test
    public void getState() {
        setMockController();
        GameState state = mock(GameState.class);
        controller.setState(state);
        assertEquals(state, controller.getState());
    }

    @Test
    public void startGame() {
        setMockController();
        when(mockHandler.getBoard()).thenReturn(mockBoard);
        controller.startGame();
        verify(mockHandler).loadMap(mockContext, 1);
        assertEquals(mockHandler.getBoard(), controller.getState().getBoard());
        // once in startGame, once in saveGame
        verify(mockHandler, times(2)).setState(controller.getState());
    }

    @Test
    public void loadGameHasExistingGame() {
        setMockController();
        when(mockHandler.getState()).thenReturn(mockState);
        assertTrue(controller.loadGame());
        verify(mockHandler).loadFromFile(mockContext);
        assertEquals(mockState, controller.getState());
    }

    @Test
    public void loadGameNoExistingGame() {
        setMockController();
        when(mockHandler.getState()).thenReturn(null);
        assertFalse(controller.loadGame());
        verify(mockHandler).loadFromFile(mockContext);
        assertNull(controller.getState());
    }

    @Test
    public void saveGame() {
        setMockController();
        controller.saveGame();
        // once in constructor, once in saveGame()
        verify(mockHandler, times(2)).setState(controller.getState());
        verify(mockHandler).saveToFile(mockContext);
    }

    @Test
    public void undoHasMadeMove() {
        setSimpleStateController();
        controller.addObserver(mockObserver);
        Board board = controller.getState().getBoard();
        // initial position
        assertEntityPosition(board.getPlayer(), 2, 1);
        assertEntityPosition(board.getGhost(), 0, 2);
        // made one move p:(2,1)->(2,0); g:(0,2)->(0,1)->(1,1)
        controller.processEvent(Direction.LEFT);
        verify(mockObserver, times(3)).update(controller, GameController.BOARD_CHANGE);
        assertEntityPosition(board.getPlayer(), 2, 0);
        assertEntityPosition(board.getGhost(), 1, 1);
        // undo
        controller.undo();
        verify(mockObserver, times(4)).update(controller, GameController.BOARD_CHANGE);
        assertEntityPosition(board.getPlayer(), 2, 1);
        assertEntityPosition(board.getGhost(), 0, 2);
    }

    @Test
    public void undoNoMadeMove() {
        setSimpleStateController();
        controller.addObserver(mockObserver);
        Board board = controller.getState().getBoard();
        // initial position
        assertEntityPosition(board.getPlayer(), 2, 1);
        assertEntityPosition(board.getGhost(), 0, 2);
        // undo
        controller.undo();
        verify(mockObserver).update(controller, GameController.BOARD_CHANGE);
        assertEntityPosition(board.getPlayer(), 2, 1);
        assertEntityPosition(board.getGhost(), 0, 2);
    }

    /**
     * Assertion of if an entity is in expected position.
     * @param entity entity to check
     * @param expectedRow expected row
     * @param expectedCol expected column
     */
    private void assertEntityPosition(Entity entity, int expectedRow, int expectedCol) {
        assertEquals(expectedRow, entity.getRow());
        assertEquals(expectedCol, entity.getCol());
    }

    @Test
    public void restart() {
        when(mockHandler.getBoard()).thenReturn(makeSimpleBoard());
        setSimpleStateController();
        controller.addObserver(mockObserver);
        Board board = controller.getState().getBoard();
        // initial position
        assertEntityPosition(board.getPlayer(), 2, 1);
        assertEntityPosition(board.getGhost(), 0, 2);
        // made one move p(2,1)->(2,0); g(0,2)->(0,1)->(1,1)
        controller.processEvent(Direction.LEFT);
        verify(mockObserver, times(3)).update(controller, GameController.BOARD_CHANGE);
        assertEntityPosition(board.getPlayer(), 2, 0);
        assertEntityPosition(board.getGhost(), 1, 1);
        // restart level
        controller.restart();
        verify(mockHandler).loadMap(mockContext, 1);
        verify(mockObserver, times(4)).update(controller, GameController.BOARD_CHANGE);
        board = controller.getState().getBoard();
        assertEntityPosition(board.getPlayer(), 2, 1);
        assertEntityPosition(board.getGhost(), 0, 2);
    }

    @Test
    public void processEventInvalidMove() {
        setSimpleStateController();
        controller.addObserver(mockObserver);
        Board board = controller.getState().getBoard();
        // initial position
        assertEntityPosition(board.getPlayer(), 2, 1);
        assertEntityPosition(board.getGhost(), 0, 2);
        // made one invalid move
        controller.processEvent(Direction.DOWN);
        verify(mockObserver, times(3)).update(controller, GameController.BOARD_CHANGE);
        assertEntityPosition(board.getPlayer(), 2, 1);
        assertEntityPosition(board.getGhost(), 1, 1);
    }

    @Test
    public void processEventValidMoveNoLevelChangeNoGameQuit() {
        setSimpleStateController();
        controller.addObserver(mockObserver);
        Board board = controller.getState().getBoard();
        // initial position
        assertEntityPosition(board.getPlayer(), 2, 1);
        assertEntityPosition(board.getGhost(), 0, 2);
        // made one move p(2,1)->(2,0); g(0,2)->(0,1)->(1,1)
        controller.processEvent(Direction.LEFT);
        verify(mockObserver, times(3)).update(controller, GameController.BOARD_CHANGE);
        assertEntityPosition(board.getPlayer(), 2, 0);
        assertEntityPosition(board.getGhost(), 1, 1);
    }

    @Test
    public void processEventValidMoveNoLevelChangeGameQuit() {
        setSimpleStateController();
        controller.addObserver(mockObserver);
        Board board = controller.getState().getBoard();
        // initial position
        assertEntityPosition(board.getPlayer(), 2, 1);
        assertEntityPosition(board.getGhost(), 0, 2);
        // made one move p(2,1)->(2,2); g(0,2)->(1,2)->(2,2)
        controller.processEvent(Direction.RIGHT);
        verify(mockObserver, times(2)).update(controller, GameController.BOARD_CHANGE);
        verify(mockObserver).update(controller, GameController.GAME_OVER);
        assertEntityPosition(board.getPlayer(), 2, 2);
        assertEntityPosition(board.getGhost(), 2, 2);
    }

    @Test
    public void processEventValidMoveLevelChangeNoGameFinish() {
        when(mockHandler.getBoard()).thenReturn(mockBoard);
        setSimpleStateController();
        controller.addObserver(mockObserver);
        Board board = controller.getState().getBoard();
        // initial position
        assertEntityPosition(board.getPlayer(), 2, 1);
        assertEntityPosition(board.getGhost(), 0, 2);
        // made one move p(2,1)->(2,0); g(0,2)->(0,1)->(1,1)
        controller.processEvent(Direction.LEFT);
        verify(mockObserver, times(3)).update(controller, GameController.BOARD_CHANGE);
        assertEntityPosition(board.getPlayer(), 2, 0);
        assertEntityPosition(board.getGhost(), 1, 1);
        // made one move p(2,0)->(1,0)
        controller.processEvent(Direction.UP);
        verify(mockObserver).update(controller, GameController.LEVEL_OVER);
        verify(mockObserver, times(3)).update(controller, GameController.BOARD_CHANGE);
        verify(mockHandler).loadMap(mockContext, 2);
        assertEquals(mockBoard, controller.getState().getBoard());
    }

    @Test
    public void processEventValidMoveLevelChangeGameFinish() {
        setSimpleStateController();
        controller.addObserver(mockObserver);
        Board board = controller.getState().getBoard();
        board.setLevel(GameState.MAX_LEVEL); // set level to maximum level
        // initial position
        assertEntityPosition(board.getPlayer(), 2, 1);
        assertEntityPosition(board.getGhost(), 0, 2);
        // made one move p(2,1)->(2,0); g(0,2)->(0,1)->(1,1)
        controller.processEvent(Direction.LEFT);
        verify(mockObserver, times(3)).update(controller, GameController.BOARD_CHANGE);
        assertEntityPosition(board.getPlayer(), 2, 0);
        assertEntityPosition(board.getGhost(), 1, 1);
        // made one move p(2,0)->(1,0)
        controller.processEvent(Direction.UP);
        verify(mockObserver).update(controller, GameController.GAME_FINISH);
        verify(mockObserver, times(3)).update(controller, GameController.BOARD_CHANGE);
        assertEntityPosition(board.getPlayer(), 1, 0);
    }

    @Test
    public void checkStackNoOverflow() {
        setSimpleStateController();
        Stack<Direction> newPlayerStack = new Stack<>();
        Stack<Direction> newGhostStack = new Stack<>();
        for (int i = 0; i < GameController.MAX_UNDO - 1; i++) {
            newPlayerStack.push(Direction.UP);
            newGhostStack.push(Direction.UP);
            newGhostStack.push(Direction.UP);
        }
        controller.setPlayerUndoStack(newPlayerStack);
        controller.setGhostUndoStack(newGhostStack);
        // make one move
        controller.processEvent(Direction.LEFT);
        // check no overflow
        assertEquals(GameController.MAX_UNDO, controller.getPlayerUndoStack().size());
        assertEquals(GameController.MAX_UNDO * 2, controller.getGhostUndoStack().size());
    }

    @Test
    public void checkStackOverflow() {
        setSimpleStateController();
        Stack<Direction> newPlayerStack = new Stack<>();
        Stack<Direction> newGhostStack = new Stack<>();
        for (int i = 0; i < GameController.MAX_UNDO; i++) {
            newPlayerStack.push(Direction.UP);
            newGhostStack.push(Direction.UP);
            newGhostStack.push(Direction.UP);
        }
        controller.setPlayerUndoStack(newPlayerStack);
        controller.setGhostUndoStack(newGhostStack);
        // make one move
        controller.processEvent(Direction.LEFT);
        // check overflow, remove first element
        assertEquals(GameController.MAX_UNDO, controller.getPlayerUndoStack().size());
        assertEquals(GameController.MAX_UNDO * 2, controller.getGhostUndoStack().size());
    }
}