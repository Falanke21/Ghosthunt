package fall2018.csc2017.game_centre;

import android.content.Context;

/**
 * Interface for a loadable class.
 */
public interface Loadable {
    /**
     * Load a file from a specific file name.
     * @param context the context needed for file io.
     */
    void loadFromFile(Context context);
}
