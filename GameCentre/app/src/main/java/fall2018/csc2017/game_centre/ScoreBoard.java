package fall2018.csc2017.game_centre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * An abstract super class ScoreBoard.
 * Entails activities needed to be performed by scoreboards of different games.
 */
public abstract class ScoreBoard {
    /**
     * Abstract method calculates the score using an ArrayList of parameters
     *
     * @param array The information needed for calculation
     * @return the score
     */
    public abstract int calculateScore(ArrayList<Integer> array);

    /**
     * Updates currentUser's high score, if needed.
     *
     * @param score the new score from end of game
     * @param game the type of game
     */
    public void update(Integer score, Game game){
        User currentUser = CurrentStatus.getCurrentUser();
        if (score > currentUser.getScore(game)) {
            currentUser.setScore(game, score);
        }
    }

    /**
     * record new score into leader board
     */
    public void writeScore(int score, ArrayList<ArrayList> leaderBoard) {
        ArrayList a = new ArrayList();
        a.add(score);
        a.add(CurrentStatus.getCurrentUser().getUsername());
        for (int i = 0; i < leaderBoard.size(); i++) {
            if ((int) leaderBoard.get(i).get(0) <= score) {
                leaderBoard.add(i, a);
                return;
            }
        }
        leaderBoard.add(a);
    }

    /**
     * Formats each user's information into an ArrayList:
     * [int score, String username]
     *
     * @param game        the type of game
     * @param users       the user hash map
     * @param leaderBoard the array list of leader board.
     */
    public void formatUsers(Game game, Map<String, User> users, ArrayList<ArrayList> leaderBoard) {
        ArrayList<ArrayList> scoreKeyArray = new ArrayList<>();
        for (User entry : users.values()) {
            if (entry.getScore(game) != 0) {
                // 0 is the default score, implying user has not played the game yet
                // thus will not appear on leader board
                ArrayList<Object> scoreKeyPair = new ArrayList<>();
                scoreKeyPair.add(entry.getScore(game));
                scoreKeyPair.add(entry.getUsername());
                scoreKeyArray.add(scoreKeyPair);
            }
        }
        customSort(scoreKeyArray, leaderBoard);
    }

    /**
     * sorts this nested list into leader board
     * by the first element in each sub list.
     *
     * @param nestedList  a nested ArrayList
     * @param leaderBoard the array list of leader board.
     */
    private void customSort(ArrayList<ArrayList> nestedList, ArrayList<ArrayList> leaderBoard) {
        for (ArrayList pair : nestedList) {
            if (leaderBoard.contains(pair)) {
                continue;
            }
            if (leaderBoard.isEmpty()) {
                leaderBoard.add(pair);
            } else if ((int) pair.get(0) < (int) leaderBoard.get(leaderBoard.size() - 1).get(0)) {
                leaderBoard.add(pair);
            } else {
                for (int i = 0; i < leaderBoard.size(); i++) {
                    if ((int) pair.get(0) >= (int) leaderBoard.get(i).get(0)) {
                        leaderBoard.add(i, pair);
                        break;
                    }
                }
            }
        }
    }
}
