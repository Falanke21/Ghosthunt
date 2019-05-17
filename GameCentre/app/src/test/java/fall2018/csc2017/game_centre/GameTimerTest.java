package fall2018.csc2017.game_centre;

import org.junit.Before;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.*;

public class GameTimerTest {
    private GameTimer gameTimer;

    @Before
    public void setUp() {
        gameTimer = new GameTimer();
    }

    @Test
    public void testPauseActionASecond() {
        assertEquals(0, gameTimer.getTotalTime());
        gameTimer.resumeAction();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                assertTrue(gameTimer.getTotalTime() > 0);
            }
        };
        timer.schedule(timerTask, 1000);
        gameTimer.pauseAction();

    }

    @Test
    public void testPauseActionTwoSecond() {
        assertEquals(0, gameTimer.getTotalTime());
        gameTimer.resumeAction();
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                assertTrue(gameTimer.getTotalTime() > 0);
            }
        };
        timer.schedule(timerTask, 2000);
        gameTimer.pauseAction();

    }
}