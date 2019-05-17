package fall2018.csc2017.game_centre.slidingtiles;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import fall2018.csc2017.game_centre.CurrentStatus;
import fall2018.csc2017.game_centre.Loadable;
import fall2018.csc2017.game_centre.Savable;

import static android.content.Context.MODE_PRIVATE;

/**
 * Model class, excluded from unit test.
 * File saver for slidingTile game. Implemented as a singleton class.
 */
class SlidingTilesFileHandler implements Savable, Loadable {

    /**
     * The main save file.
     */
    private static final String SAVE_FILENAME = "save_file.ser";

    /**
     * Mapping from username to corresponding board manager.
     */
    private Map<String, GameState> boardManagers;

    /**
     * The board manager.
     */
    private GameState gameState;

    /**
     * Tag for logging.
     */
    private static final String LOG_TAG = "SlidingTilesFileHandler";

    /**
     * The fileSaver instance.
     */
    private static SlidingTilesFileHandler fileSaver;

    /**
     * Private constructor for singleton.
     */
    private SlidingTilesFileHandler() {
    }

    /**
     * The getter for Singleton File Saver.
     *
     * @return the file saver as needed
     */
    static SlidingTilesFileHandler getInstance() {
        if (fileSaver == null) {
            fileSaver = new SlidingTilesFileHandler();
        }
        return fileSaver;
    }

    /**
     * Load the board manager from fileName.
     * @param context  The context that got adapted from activity
     *
     */
    public void loadFromFile(Context context) {
        try {
            InputStream inputStream = context.openFileInput(SlidingTilesFileHandler.SAVE_FILENAME);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManagers = (HashMap<String, GameState>) input.readObject();
                gameState = boardManagers.get(CurrentStatus.getCurrentUser().getUsername());
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e(LOG_TAG, "File contained unexpected data type: " + e.toString());
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Calling on null reference: " + e.toString());
        }
    }

    /**
     * Save the board manager to fileName.
     *
     * @param context  the activity
     */
    public void saveToFile(Context context) {
        try {
            if (boardManagers == null) {
                boardManagers = new HashMap<>();
            }
            boardManagers.put(CurrentStatus.getCurrentUser().getUsername(), gameState);
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput(SlidingTilesFileHandler.SAVE_FILENAME, MODE_PRIVATE));
            outputStream.writeObject(boardManagers);
            outputStream.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "File write failed: " + e.toString());
        }
    }

    /**
     * The getter for GameState.
     *
     * @return The instance of gameState.
     */
    GameState getGameState() {
        return gameState;
    }

    /**
     * The setter for GameState.
     *
     * @param obj the gameState that's to be set.
     */
    void setGameState(GameState obj) {
        gameState = obj;
    }
}
