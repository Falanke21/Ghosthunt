package fall2018.csc2017.game_centre.ghost_hunt;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import fall2018.csc2017.game_centre.ScoreBoard;

import static org.junit.Assert.*;

public class GhostHuntScoreboardControllerTest {

    /**
     * Scoreboard to test.
     */
    private ScoreBoard scoreBoard;

    @Before
    public void setUp() {
        scoreBoard = new GhostHuntScoreboardController();
    }

    @After
    public void tearDown() {
        scoreBoard = null;
    }

    @Test
    public void calculateScore() {
        ArrayList<Integer> res = new ArrayList<>();
        res.add(50);
        res.add(70);
        assertEquals(313, scoreBoard.calculateScore(res));
        res.set(0, 100);
        res.set(1, 120);
        assertEquals(161, scoreBoard.calculateScore(res));
    }
}