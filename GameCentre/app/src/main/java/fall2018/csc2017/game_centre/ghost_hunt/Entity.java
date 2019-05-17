package fall2018.csc2017.game_centre.ghost_hunt;

import java.io.Serializable;

/**
 * Model
 *
 * Abstract all entity in ghost hunt.
 */
abstract class Entity implements Serializable {

    /**
     * Facing direction.
     */
    Direction direction;

    /**
     * Row position.
     */
    int row;

    /**
     * Column position.
     */
    int col;

    /**
     * Constructor of an entity.
     * @param row initial row position
     * @param col initial column position
     */
    Entity(int row, int col) {
        this.row = row;
        this.col = col;
        this.direction = Direction.DOWN;
    }

    /**
     * Get row position of the entity.
     * @return row position
     */
    int getRow() {
        return this.row;
    }

    /**
     * Get column position of the entity.
     * @return column position
     */
    int getCol() {
        return this.col;
    }

    /**
     * Setter for direction.
     * @param direction new direction
     */
    void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Entity makes a move.
     * @param move direction of move
     */
    void move(Direction move) {
        switch (move) {
            case UP: row--; break;
            case DOWN: row++; break;
            case LEFT: col--; break;
            case RIGHT: col++; break;
        }
        this.direction = move;
    }

    /**
     * Get the resource of the entity based on direction.
     * @return resource id
     */
    abstract int getResource();
}
