package fall2018.csc2017.game_centre.ghost_hunt;

import java.io.Serializable;

import fall2018.csc2017.game_centre.R;

/**
 * Model
 *
 * Simulates a player.
 */
class Player extends Entity implements Serializable {

    /**
     * Inherits parent constructor.
     * @param row initial row position
     * @param col initial column position
     */
    Player(int row, int col) {
        super(row, col);
    }

    /**
     * Get the resource of the player based on direction.
     * @return resource id
     */
    @Override
    int getResource() {
        int res;
        switch (direction) {
            case UP: res = R.drawable.char_back; break;
            case DOWN: res = R.drawable.char_front; break;
            case LEFT: res = R.drawable.char_left; break;
            case RIGHT: res = R.drawable.char_right; break;
            default: res = R.drawable.char_front; break;
        }
        return res;
    }
}
