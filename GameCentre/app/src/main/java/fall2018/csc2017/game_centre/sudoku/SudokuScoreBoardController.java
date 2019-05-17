package fall2018.csc2017.game_centre.sudoku;

import java.util.ArrayList;
import fall2018.csc2017.game_centre.ScoreBoard;


/**
 * The scoreboard controller for Sudoku scoreboard.
 */
public class SudokuScoreBoardController extends ScoreBoard {

    /**
     * calculate score of game
     *
     * @param array an ArrayList of length 3, index 0 is number of hints remaining,
     *              index 1 is the number of wrongs, and index 2 is time in seconds.
     * @return the score of game
     */
    public int calculateScore(ArrayList<Integer> array) {
        int hintLeft = array.get(0);
        int wrong = array.get(1);
        int time_in_sec = array.get(2);
        int score = 10000 - (100 * wrong) - time_in_sec + (100 * hintLeft);
        return Math.max(1, score); // for user experience there will not be a negative score
    }
}
