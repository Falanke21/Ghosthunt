package fall2018.csc2017.game_centre.ghost_hunt;

import android.content.Context;

import java.util.Observable;
import java.util.Stack;

import fall2018.csc2017.game_centre.Undoable;

/**
 * Controller
 *
 * Handle game events.
 */
class GameController extends Observable implements Undoable {

    /**
     * Argument indicating change of board.
     */
    static final Integer BOARD_CHANGE = 0;

    /**
     * Argument indicating end of game.
     */
    static final Integer GAME_OVER = 1;

    /**
     * Argument indicating level is over.
     */
    static final Integer LEVEL_OVER = 2;

    /**
     * Argument indicating finish of game.
     */
    static final Integer GAME_FINISH = 3;

    /**
     * How many moves a ghost can make in a round.
     */
    private static final int GHOST_MOVE_PER_ROUND = 2;

    /**
     * Context of the activity.
     */
    private Context context;

    /**
     * File handler for the controller.
     */
    private FileHandler fileHandler;

    /**
     * Board manager.
     */
    private GameState state;

    /**
     * Determine if setting next level.
     */
    private boolean isSettingNextLevel;

    /**
     * Maximum of undo time.
     */
    static final int MAX_UNDO = 5;

    /**
     * Player move undo stack.
     */
    private Stack<Direction> playerUndoStack = new Stack<>();

    /**
     * Ghost move undo stack.
     */
    private Stack<Direction> ghostUndoStack = new Stack<>();

    /**
     * Constructor for controller.
     * @param context context of activity
     */
    GameController(Context context, GameState state) {
        this.context = context;
        this.state = state;
        this.fileHandler = FileHandler.getInstance();
        this.fileHandler.setState(this.state);
        this.isSettingNextLevel = false;
    }

    /**
     * Constructor taking in a file handler. For unit-testing purpose.
     * @param context context
     * @param handler file handler
     * @param state game state
     */
    GameController(Context context, FileHandler handler, GameState state) {
        this.context = context;
        this.state = state;
        this.fileHandler = handler;
        this.fileHandler.setState(this.state);
        this.isSettingNextLevel = false;
    }

    /**
     * Setter for context
     * @param context context
     */
    void setContext(Context context) {
        this.context = context;
    }

    /**
     * Getter for board manager.
     * @return board manager
     */
    GameState getState() {
        return this.state;
    }

    /**
     * Setter for state.
     * @param state new state
     */
    void setState(GameState state) {
        this.state = state;
    }

    /**
     * Start new game.
     */
    void startGame() {
        this.fileHandler.loadMap(context, 1);
        this.state = new GameState(fileHandler.getBoard());
        this.fileHandler.setState(state);
        saveGame();
    }

    /**
     * Load existing game.
     * @return if there is saved game
     */
    boolean loadGame() {
        fileHandler.loadFromFile(context);
        this.state = fileHandler.getState();
        return state != null;
    }

    /**
     * Save the game.
     */
    void saveGame() {
        fileHandler.setState(this.state);
        fileHandler.saveToFile(context);
    }

    /**
     * Performs a move undo on the game.
     */
    @Override
    public void undo() {
        Direction direction = playerUndoStack.size() != 0 ? playerUndoStack.pop() : null;
        if (direction != null) {
            state.getBoard().getPlayer().move(direction);
        }
        for (int i = 0; i < GHOST_MOVE_PER_ROUND; i++) {
            direction = ghostUndoStack.size() != 0 ? ghostUndoStack.pop() : null;
            if (direction != null) {
                state.getBoard().getGhost().move(direction);
            }
        }
        notifyChange();
    }

    /**
     * Restart the game.
     */
    void restart() {
        int current_level = this.state.getBoard().getLevel();
        fileHandler.loadMap(context, current_level);
        this.state.setBoard(fileHandler.getBoard());
        playerUndoStack.clear();
        ghostUndoStack.clear();
        notifyChange();
    }

    /**
     * Process direction change.
     * @param direction direction of going
     */
    void processEvent(Direction direction) {
        Board board = state.getBoard();
        Player player = board.getPlayer();
        Ghost ghost = board.getGhost();
        processEntityMove(player, direction);
        state.incrementMoveCount();
        if (isSettingNextLevel) {
            saveGame();
            isSettingNextLevel = false;
        } else {
            for (int i = 0; i < GHOST_MOVE_PER_ROUND; i++) {
                Direction nextDir = ghost.getNextDirection(player.getRow(), player.getCol());
                processEntityMove(ghost, nextDir);
            }
        }
    }

    /**
     * Process an entity's move on certain direction.
     * @param entity entity to make move
     * @param direction direction of move
     */
    private void processEntityMove(Entity entity, Direction direction) {
        if (isValidMove(entity.getRow(), entity.getCol(), direction)) {
            entity.move(direction);
            appendMove(entity, direction, true);
        } else {
            entity.setDirection(direction);
            appendMove(entity, direction, false);
        }
        notifyChange();
    }

    /**
     * Append move into undo stack.
     * @param entity entity makes move
     * @param direction direction of move
     * @param isValid if move is valid
     */
    private void appendMove(Entity entity, Direction direction, boolean isValid) {
        Direction counterDir = getCounterDirection(direction);
        if (entity instanceof Player) {
            playerUndoStack.add(isValid ? counterDir : null);
            checkStack(playerUndoStack, MAX_UNDO);
        } else if (entity instanceof Ghost) {
            ghostUndoStack.add(isValid ? counterDir : null);
            checkStack(ghostUndoStack, MAX_UNDO * 2);
        }
    }

    /**
     * Return a counter direction based on an existing direction.
     * @param direction direction
     * @return counter direction
     */
    private Direction getCounterDirection(Direction direction) {
        Direction res = null;
        switch (direction) {
            case UP: res = Direction.DOWN; break;
            case DOWN: res = Direction.UP; break;
            case LEFT: res = Direction.RIGHT; break;
            case RIGHT: res = Direction.LEFT; break;
        }
        return res;
    }

    /**
     * Check if undo stack is over-populated. If so, remove the beginning.
     * @param undoStack the stack to check size
     * @param maxUndo maximum undo times
     */
    private void checkStack(Stack<Direction> undoStack, int maxUndo) {
        if (undoStack.size() > maxUndo) {
            undoStack.remove(0);
        }
    }

    /**
     * Determine if the move is a valid one.
     * @param direction direction to move
     * @return if the move is valid or not
     */
    private boolean isValidMove(int row, int col, Direction direction) {
        Board board = state.getBoard();
        return board.getTile(row, col).getAvailableMoves().contains(direction);
    }

    /**
     * Notify change of the board accordingly.
     */
    private void notifyChange() {
        setChanged();
        if (gameOver()) {
            notifyObservers(GAME_OVER);
        } else {
            if (levelOver()) {
                if (state.getBoard().getLevel() < GameState.MAX_LEVEL) {
                    isSettingNextLevel = true;
                    setNextLevel();
                    notifyObservers(LEVEL_OVER);
                } else {
                    notifyObservers(GAME_FINISH);
                }
            } else {
                notifyObservers(BOARD_CHANGE);
            }
        }
    }

    /**
     * Set the board to next level.
     */
    private void setNextLevel() {
        int current_level = this.state.getBoard().getLevel();
        fileHandler.loadMap(context, current_level + 1);
        this.state.setBoard(fileHandler.getBoard());
    }

    /**
     * Determine if the current level is finished.
     * @return if level completed
     */
    private boolean levelOver() {
        Board board = state.getBoard();
        int row = board.getPlayer().getRow();
        int col = board.getPlayer().getCol();
        return board.getTile(row, col).isExit();
    }

    /**
     * Determine if the game is over.
     * @return if game is over
     */
    private boolean gameOver() {
        Player p = state.getBoard().getPlayer();
        Ghost g = state.getBoard().getGhost();
        return p.getRow() == g.getRow() && p.getCol() == g.getCol();
    }

    /**
     * Getter for context. For unit-testing purpose.
     * @return context of the controller
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * Setter for player undo stack. For unit-testing purpose.
     * @param playerUndoStack player undo stack
     */
    void setPlayerUndoStack(Stack<Direction> playerUndoStack) {
        this.playerUndoStack = playerUndoStack;
    }

    /**
     * Setter for ghost undo stack. For unit-testing purpose.
     * @param ghostUndoStack ghost undo stack
     */
    void setGhostUndoStack(Stack<Direction> ghostUndoStack) {
        this.ghostUndoStack = ghostUndoStack;
    }

    /**
     * Getter for player undo stack. For unit-testing purpose.
     * @return player undo stack
     */
    Stack<Direction> getPlayerUndoStack() {
        return this.playerUndoStack;
    }

    /**
     * Getter for ghost undo stack. For unit-testing purpose.
     * @return ghost undo stack
     */
    Stack<Direction> getGhostUndoStack() {
        return this.ghostUndoStack;
    }
}
