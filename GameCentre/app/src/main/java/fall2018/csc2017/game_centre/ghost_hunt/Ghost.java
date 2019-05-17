package fall2018.csc2017.game_centre.ghost_hunt;

import java.io.Serializable;

import fall2018.csc2017.game_centre.R;

/**
 * Model
 *
 * Simulating a ghost.
 */
class Ghost extends Entity implements Serializable {

    /**
     * Constructor taking in ghost's coordinate and player's coordinate.
     * @param row ghost's row position
     * @param col ghost's column position
     */
    Ghost(int row, int col) {
        super(row, col);
    }

    /**
     * Get the resource of the ghost based on direction.
     * @return resource id
     */
    @Override
    int getResource() {
        int res;
        switch (direction) {
            case UP: res = R.drawable.ghost_back; break;
            case DOWN: res = R.drawable.ghost_front; break;
            case LEFT: res = R.drawable.ghost_left; break;
            case RIGHT: res = R.drawable.ghost_right; break;
            default: res = R.drawable.ghost_front; break;
        }
        return res;
    }

    /**
     * Calculate ghost's next position according to player's position.
     * @return ghost's next move
     */
    Direction getNextDirection(int playerRow, int playerCol) {
        int v_diff = playerRow - row;
        int h_diff = playerCol - col;
        if (v_diff >= h_diff) {
            if (v_diff > (-1) * h_diff) {
                return Direction.DOWN;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (v_diff > (-1) * h_diff) {
                return Direction.RIGHT;
            } else {
                return Direction.UP;
            }
        }
    }
}
