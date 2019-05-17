package fall2018.csc2017.game_centre.slidingtiles;

import java.util.ArrayList;

import fall2018.csc2017.game_centre.ScoreBoard;

/**
 * ScoreBoard controller for sliding tiles.
 */
public class SlidingTilesScoreBoardController extends ScoreBoard {

    /**
     * calculate score of game
     *
     * @param array an ArrayList of length 2, index 0 is moves, index 1 is time in seconds
     * @return the score of game
     */
    public int calculateScore(ArrayList<Integer> array) {
        float moves = array.get(0);
        float time_in_sec = array.get(1);
        double dbl_score = 1 / ((moves * 15.0) + time_in_sec);
        return (int) Math.ceil(dbl_score * 100000);
    }
}
