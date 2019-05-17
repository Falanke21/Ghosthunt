package fall2018.csc2017.game_centre.ghost_hunt;

import java.util.ArrayList;
import fall2018.csc2017.game_centre.ScoreBoard;

/**
 * The ghost hunt scoreboard controller.
 */
class GhostHuntScoreboardController extends ScoreBoard {

    /**
     * calculate score of game
     *
     * @param array an ArrayList of length 2, index 0 is moves, index 1 is time in seconds
     */
    public int calculateScore(ArrayList<Integer> array) {
        float move = array.get(0);
        float time_in_sec = array.get(1);
        double dbl_score = 1 / ((move * 5) + time_in_sec);
        return (int) Math.round(dbl_score * 100000);
    }
}
