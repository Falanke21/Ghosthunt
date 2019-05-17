package fall2018.csc2017.game_centre.sudoku;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fall2018.csc2017.game_centre.CurrentStatus;
import fall2018.csc2017.game_centre.Game;
import fall2018.csc2017.game_centre.Loadable;
import fall2018.csc2017.game_centre.Savable;

import static android.content.Context.MODE_PRIVATE;

/**
 * Model class, exclude from unit test.
 *
 * Deal with file input/output for sudoku. Singleton class.
 */
class SudokuFileHandler implements Savable, Loadable {
    /**
     * The Log tag of logging.
     */
    private static final String LOG_TAG = "SudokuFileHandler";
    /**
     * The state of current game.
     */
    private SudokuGameState gameState;

    /**
     * The file handler instance/
     */
    private static SudokuFileHandler fileHandler;

    /**
     * The file handler for sudoku game state.
     */
    private SudokuFileHandler(){}

    /**
     * Getter for File handler
     * @return the file handler instance.
     */
    static SudokuFileHandler getInstance(){
        if (fileHandler == null){
            fileHandler = new SudokuFileHandler();
        }
        return fileHandler;
    }

    /**
     * Load the game state from user's fileName.
     * @param context  The context that got adapted from activity
     */
    public void loadFromFile(Context context) {
        try {
            InputStream inputStream = context.openFileInput(
                    CurrentStatus.getCurrentUser().getUserFilename(Game.Sudoku));
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                gameState = (SudokuGameState) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "File not found: " + e.toString()); gameState = null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Can not read file: " + e.toString()); gameState = null;
        } catch (ClassNotFoundException e) {
            Log.e(LOG_TAG, "File unexpected type: " + e.toString()); gameState = null;
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "Calling on null reference: " + e.toString()); gameState = null;
        }
    }

    /**
     * Save the game state to fileName.
     *
     * @param context  the activity
     */
    public void saveToFile(Context context) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(context.openFileOutput(
                    CurrentStatus.getCurrentUser().getUserFilename(Game.Sudoku), MODE_PRIVATE));
            outputStream.writeObject(gameState);
            outputStream.close();
        } catch (Exception e){
            Log.e(LOG_TAG, "File write failed: " + e.toString());
        }
    }

    /**
     * Setter for the game state.
     * @param gameState the game state that gets set.
     */
    void setGameState(SudokuGameState gameState) {
        this.gameState = gameState;
    }

    /**
     * Getter for game state.
     * @return the game state in file handler.
     */
    SudokuGameState getGameState() {
        return gameState;
    }
}
