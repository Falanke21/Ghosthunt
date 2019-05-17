package fall2018.csc2017.game_centre.ghost_hunt;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import fall2018.csc2017.game_centre.CurrentStatus;
import fall2018.csc2017.game_centre.Game;
import fall2018.csc2017.game_centre.Loadable;
import fall2018.csc2017.game_centre.Savable;

/**
 * Model
 *
 * Handler for file IO in Ghost Hunt. Made into singleton.
 */
class FileHandler implements Savable, Loadable {

    /**
     * Sole instance of file handler.
     */
    private static final FileHandler INSTANCE = new FileHandler();

    /**
     * Data name prefix for ghost hunt.
     */
    private static final String MAP_DATA_PREFIX = "ghost_hunt_map";

    /**
     * Logging tag.
     */
    private final String LOG_TAG = "GhostHuntFileHandler";

    /**
     * Loaded map data from file.
     */
    private Board board;

    /**
     * Board manager.
     */
    private GameState state;

    /**
     * Private constructor for singleton.
     */
    private FileHandler() {}

    /**
     * Return the singleton instance.
     * @return sole instance of file handler
     */
    static FileHandler getInstance() {
        return INSTANCE;
    }

    void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Getter got the board.
     * @return loaded map
     */
    Board getBoard() {
        return this.board;
    }

    /**
     * Getter for board manager.
     * @return board manager
     */
    GameState getState() {
        return this.state;
    }

    /**
     * Setter for board manager.
     * @param state board manager
     */
    void setState(GameState state) {
        this.state = state;
    }

    /**
     * Load data from current user's file.
     * @param context context
     */
    public void loadFromFile(Context context) {
        String fileName = CurrentStatus.getCurrentUser().getUserFilename(Game.GhostHunt);
        try {
            InputStream inputStream = context.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                state = (GameState) input.readObject();
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
     * Save data to current user's file.
     * @param context context
     */
    public void saveToFile(Context context) {
        String fileName = CurrentStatus.getCurrentUser().getUserFilename(Game.GhostHunt);
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStream.writeObject(state);
            outputStream.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "File write failed: " + e.toString());
        }
    }

    /**
     * Load level map from CSV file.
     * @param context context
     * @param level level of map
     */
    void loadMap(Context context, int level) {
        String fileName = MAP_DATA_PREFIX + level;
        int id = context.getResources().getIdentifier(fileName, "raw", context.getPackageName());
        InputStream inputStream = context.getResources().openRawResource(id);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        try {
            String line = reader.readLine();
            Player player = (Player) readEntity(line, Player.class);
            line = reader.readLine();
            Ghost ghost = (Ghost) readEntity(line, Ghost.class);
            line = reader.readLine();
            int[] exit = readExit(line);
            int row = 0;
            int GRID_SIZE = 8;
            Tile[][] grid = new Tile[GRID_SIZE][GRID_SIZE];
            while ((line = reader.readLine()) != null) {
                grid[row] = readTile(line);
                row++;
            }
            grid[exit[0]][exit[1]].setExit();
            this.board = new Board(level, grid, player, ghost);
        } catch (IOException e) {
            Log.wtf(LOG_TAG, "No map data file found: " + e.toString());
        }
    }

    /**
     * Read exit position on the map.
     * @param line line read from file
     * @return exit location
     */
    private int[] readExit(String line) {
        String[] tokens = line.split(",");
        int row = Integer.parseInt(tokens[0]);
        int col = Integer.parseInt(tokens[1]);
        return new int[]{row, col};
    }

    /**
     * Read current line for a new entity.
     * @param line line read from file
     * @param type type of entity
     * @return constructed entity
     */
    private Entity readEntity(String line, Class type) {
        String[] tokens = line.split(",");
        int row = Integer.parseInt(tokens[0]);
        int col = Integer.parseInt(tokens[1]);
        return type == Player.class ? new Player(row, col) : new Ghost(row, col);
    }

    /**
     * Read tiles in one row.
     * @param line line read from file
     * @return a list of tiles in a row
     */
    Tile[] readTile(String line) {
        ArrayList<Tile> tileArray = new ArrayList<>();
        String[] tokens = line.split(",");
        for (String token : tokens) {
            ArrayList<Direction> availableMoves = new ArrayList<>();
            if (token.charAt(0) == '1') availableMoves.add(Direction.UP);
            if (token.charAt(1) == '1') availableMoves.add(Direction.RIGHT);
            if (token.charAt(2) == '1') availableMoves.add(Direction.DOWN);
            if (token.charAt(3) == '1') availableMoves.add(Direction.LEFT);
            tileArray.add(new Tile(availableMoves));
        }
        Tile[] rowTile = new Tile[tileArray.size()];
        return tileArray.toArray(rowTile);
    }
}
