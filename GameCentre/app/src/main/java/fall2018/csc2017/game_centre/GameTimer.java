package fall2018.csc2017.game_centre;

import java.io.Serializable;

/**
 * The timer that is used by each game to calculate final score.
 */
public class GameTimer implements Serializable {
    /**
     * The starting time for each interval.
     */
    private long startingTime;

    /**
     * the total time from last pause.
     */
    private long totalTime;

    /**
     * Constructor when game first starts.
     */
    public GameTimer() {
        resumeAction();
    }

    /**
     * Called when OnPause.
     */
    public void pauseAction() {
        totalTime = totalTime + System.currentTimeMillis() - startingTime;
    }

    /**
     * Called when OnResume. Also need to be called when instantiate.
     */
    public void resumeAction() {
        startingTime = System.currentTimeMillis();
    }

    /**
     * Convert totalTime into seconds.
     *
     * @return an Integer in total number of seconds.
     */
    private Integer convertToSeconds() {
        long longResult = ((totalTime + System.currentTimeMillis() - startingTime) / 1000);
        return (int) longResult;
    }

    /**
     * Getter for total time in seconds.
     *
     * @return an int of total time in seconds.
     */
    public int getTotalTime() {
        return convertToSeconds();
    }
}
