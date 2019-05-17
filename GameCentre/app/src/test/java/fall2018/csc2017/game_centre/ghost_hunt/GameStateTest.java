package fall2018.csc2017.game_centre.ghost_hunt;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import fall2018.csc2017.game_centre.GameTimer;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class GameStateTest {

    /**
     * Mock board.
     */
    @Mock
    private Board mockBoard;

    /**
     * Mock timer.
     */
    @Mock
    private GameTimer mockTimer;

    /**
     * Game state to test.
     */
    private GameState state;

    /**
     * Mockito mocking rule.
     */
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        state = new GameState(mockBoard, mockTimer);
    }

    @After
    public void tearDown() {
        state = null;
    }

    @Test
    public void constructor() {
        state = new GameState(mockBoard);
        assertEquals(mockBoard, state.getBoard());
        assertEquals(0, state.getMoveCount());
        assertNotNull(state.getTimer());
    }

    @Test
    public void getBoard() {
        assertEquals(mockBoard, state.getBoard());
    }

    @Test
    public void setBoard() {
        Board newBoard = mock(Board.class);
        state.setBoard(newBoard);
        assertEquals(newBoard, state.getBoard());
    }

    @Test
    public void incrementMoveCount() {
        assertEquals(0, state.getMoveCount());
        state.incrementMoveCount();
        assertEquals(1, state.getMoveCount());
        state.incrementMoveCount();
        assertEquals(2, state.getMoveCount());
    }

    @Test
    public void getMoveCount() {
        assertEquals(0, state.getMoveCount());
    }

    @Test
    public void getTimer() {
        assertEquals(mockTimer, state.getTimer());
    }
}