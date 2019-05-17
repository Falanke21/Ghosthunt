package fall2018.csc2017.game_centre.slidingtiles;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import fall2018.csc2017.game_centre.CurrentStatus;
import fall2018.csc2017.game_centre.GameTimer;
import fall2018.csc2017.game_centre.R;

/**
 * Model class, exclude from unit test.
 * The game activity.
 */
public class SlidingTilesGameActivity extends AppCompatActivity implements Observer {

    /**
     * Tag for logging.
     */
    private static final String LOG_TAG = "SlidingTilesGameActivity";

    /**
     * Request code for user picking image from gallery.
     */
    private static final int PICK_IMAGE = 100;

    /**
     * The file saver for gameState.
     */
    private SlidingTilesFileHandler fileSaver;

    /**
     * Timer for the game.
     */
    private GameTimer timer;

    /**
     * The length of the board.
     */
    private int boardLength;

    /**
     * The board manager.
     */
    private GameState gameState;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;

    /**
     * Grid view to display.
     */
    private GestureDetectGridView gridView;

    /**
     * Calculated column height and width based on device size.
     */
    private int columnWidth, columnHeight;

    /**
     * The image processor for image slicing.
     */
    private SlidingTilesImageProcessor imageProcessor;

    /**
     * OnCreate method.
     * @param savedInstanceState inherited needed from activities.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileSaver = SlidingTilesFileHandler.getInstance();
        gameState = fileSaver.getGameState();
        boardLength = gameState.getBoard().getLength();
        gameState.getBoard().addObserver(this);

        // Add View to activity
        createTileButtons(this);
        setContentView(R.layout.activity_main);
        setUpGridView();
        // Add button listeners
        addUndoButtonListener();
        addChangeBackgroundButtonListener();
        setDisplayUsername();

        gridView.addObserverMController(this);
    }

    /**
     * Helper function for setting up the grid view in activity
     */
    private void setUpGridView(){
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(boardLength);
        gridView.setBoardManager(gameState);

        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();
                        columnWidth = displayWidth / boardLength;
                        columnHeight = displayHeight / boardLength;
                        imageProcessor = new SlidingTilesImageProcessor(
                                SlidingTilesGameActivity.this,
                                boardLength, columnWidth, columnHeight);
                        display();
                    }
                });
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        timer.pauseAction();
    }

    /**
     * OnResume method.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (timer == null){
            timer = new GameTimer();
        }
        timer.resumeAction();
    }

    /**
     * Activate SlidingTiles undo button.
     */
    private void addUndoButtonListener() {
        Button undo = findViewById(R.id.undo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridView.mController.undo();
            }
        });
    }

    /**
     * Activate SlidingTiles change background button.
     */
    private void addChangeBackgroundButtonListener() {
        Button changeBackground = findViewById(R.id.changeBackground);
        changeBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    /**
     * Open gallery for image picking and ask for result in return.
     */
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode code representing the request
     * @param resultCode condition of the result back
     * @param data data passed back
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                int gridWidth = gridView.getMeasuredWidth();
                int gridHeight = gridView.getMeasuredHeight();
                image = Bitmap.createScaledBitmap(image, gridWidth,gridHeight, true);

                imageProcessor.trimImage(image);
                updateTileButtons();
            } catch (IOException e) {
                Toast.makeText(this, "Cannot read image", Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, "Cannot read image: " + e.toString());
            }
        }
    }

    /**
     * Create the buttons for displaying the tiles. Also set up the background id for tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        Board board = gameState.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != boardLength; row++) {
            for (int col = 0; col != boardLength; col++) {
                Button tmp = new Button(context);

                Tile tile = board.getTile(row, col);
                int backgroundId = getBackgroundIdFromR(tile, boardLength * boardLength);
                tile.setBackground(backgroundId);

                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        Board board = gameState.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / boardLength;
            int col = nextPos % boardLength;
            int index = board.getTile(row, col).id;
            if (imageProcessor.getCustomImageTiles().size() == 0 || index == board.numTiles()) {
                b.setBackgroundResource(board.getTile(row, col).getBackground());
            } else {
                b.setBackground(imageProcessor.getCustomImageTiles().get(index - 1));
            }
            nextPos++;
        }
    }

    /**
     * use getResources method to find tile background id from R.
     * @param tile    the tile that is generating background id
     * @param blankId the blankId of the board
     * @return the background id of tile in R
     */
    private int getBackgroundIdFromR(Tile tile, int blankId){
        int tileId = tile.getId();
        if (tileId != blankId){
            String resource = "tile_" + Integer.toString(tileId);
            return this.getResources().getIdentifier(resource, "drawable", getPackageName());
        }
        else {
            return this.getResources().getIdentifier(
                    "tile_blank", "drawable", getPackageName());
        }
    }

    /**
     * Update board if board changes, switch to score board if movementController.
     * @param o an observable class
     * @param arg an arg used by update.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Board) {
            display();
        }
        else if (o instanceof GameController){
            if (arg instanceof String){
                Toast.makeText(this, (String)arg, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "YOU WIN!", Toast.LENGTH_LONG).show();
                switchToScoreBoard((Integer) arg);
            }
        }
    }

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new SlidingTilesAdapter(tileButtons, columnWidth, columnHeight));
        setDisplayMove();
    }

    /**
     * Switch to ScoreBoardActivity
     * @param move the number of moves that the ended game had
     */
    private void switchToScoreBoard(Integer move){
        Intent i = new Intent(this, SlidingTilesScoreBoardActivity.class);
        if (boardLength == 3) {
            i.putExtra("move", move * 10 );
        }
        else if (boardLength == 4) {
            i.putExtra("move", move * 5);
        }
        else {
            i.putExtra("move", move);
        }
        Integer time = timer.getTotalTime();
        i.putExtra("totalTime", time);

        startActivity(i);
        finish();
    }

    /**
     * set username to text view.
     */
    private void setDisplayUsername() {
        ((TextView)findViewById(R.id.player_textview)).setText(
                CurrentStatus.getCurrentUser().getUsername());
    }

    /**
     * set display move.
     */
    private void setDisplayMove() {
        String result = ((Integer) gameState.getMoveCounter()).toString();
        ((TextView)findViewById(R.id.move_textview)).setText(result);
    }
}
