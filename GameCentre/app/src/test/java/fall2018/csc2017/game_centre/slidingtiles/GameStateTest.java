package fall2018.csc2017.game_centre.slidingtiles;

import org.junit.Before;
import org.junit.Test;

import java.util.EmptyStackException;

import static org.junit.Assert.*;

public class GameStateTest {

    private GameState gameState;

    @Before
    public void setUp() {
        gameState = new GameState(1, 3);
    }

    @Test
    public void testSaveMove() {
        gameState.saveMove(1, 1);
        int[] result = gameState.getLastMove();
        assertEquals(1, result[0]);
        assertEquals(1, result[1]);
    }

    @Test(expected=EmptyStackException.class)
    public void testSaveMoveReachMax() {
        gameState.saveMove(0, 0);
        gameState.saveMove(0, 1);
        gameState.getLastMove();
        gameState.getLastMove();

    }
}