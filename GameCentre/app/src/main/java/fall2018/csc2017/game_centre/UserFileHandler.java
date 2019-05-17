package fall2018.csc2017.game_centre;

import android.content.Context;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Model class, exclude from unit test
 * File handler for all users data.
 */
public class UserFileHandler implements Savable, Loadable{

    /**
     * Tag for logging.
     */
    private static final String LOG_TAG = "UserFileHandler";

    /**
     * File name of Users HashMap
     */
    private static final String FILE_NAME = "users.ser";

    /**
     * Mapping from username to user.
     */
    private Map<String, User> users;

    /**
     * The fileSaver instance.
     */
    private static UserFileHandler fileSaver;

    /**
     * Private constructor for singleton.
     */
    private UserFileHandler() {
    }

    /**
     * The getter for Singleton File Saver.
     *
     * @return the file saver as needed
     */
    public static UserFileHandler getInstance() {
        if (fileSaver == null) {
            fileSaver = new UserFileHandler();
        }
        return fileSaver;
    }

    /**
     * @return the users Map that maps the user name with the corresponding user.
     */
    public Map<String, User> getUsers() {
        return users;
    }

    /**
     * Load users from filename.
     * @param context the context needed for file io.
     */
    public void loadFromFile(Context context) {
        try {
            InputStream inputStream = context.openFileInput(FILE_NAME);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                users = (HashMap<String, User>) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e(LOG_TAG, "File contained unexpected data type: " + e.toString());
        } finally {
            if (users == null) {
                users = new HashMap<>();
            }
        }
    }

    /**
     * Save users to filename.
     * @param context the context needed for file io.
     */
    public void saveToFile(Context context) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput(FILE_NAME, MODE_PRIVATE));
            outputStream.writeObject(users);
            outputStream.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "File write failed: " + e.toString());
        }
    }
}
