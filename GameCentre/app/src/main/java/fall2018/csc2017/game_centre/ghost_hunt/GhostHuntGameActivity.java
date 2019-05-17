package fall2018.csc2017.game_centre.ghost_hunt;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import fall2018.csc2017.game_centre.R;

/**
 * View
 *
 * Activity for Ghost Hunt game.
 */
public class GhostHuntGameActivity extends AppCompatActivity implements Observer {

    /**
     * Mark indicating game is quit.
     */
    static final int GAME_QUIT = 10;

    /**
     * Mark indicating game is finished.
     */
    static final int GAME_FINISH = 11;

    /**
     * Number of rows in the board.
     */
    private int rowNum;

    /**
     * Number of columns in the board.
     */
    private int colNum;

    /**
     * Handler for motion events.
     */
    private GameController gameController;

    /**
     * Grid view of the map.
     */
    private GridView gridView;

    /**
     * Views of the tiles in the grid.
     */
    private ArrayList<ImageView> tileViews;

    /**
     * Tile width.
     */
    private int tileWidth;

    /**
     * Tile height.
     */
    private int tileHeight;

    /**
     * Action when activity is created.
     * @param savedInstanceState previous saved state bundle
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getIntent().getExtras();
        if (extra == null) {
            gameController = new GameController(this, null);
        } else {
            GameState state = (GameState) extra.get(GameState.INTENT_NAME);
            gameController = new GameController(this, state);
        }
        gameController.addObserver(this);
        setContentView(R.layout.activity_ghost_game);
        setUpGridView();
        addDirectionButtonListener();
        addUndoButtonListener();
        addRestartButtonListener();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        quitGame();
        super.onBackPressed();
    }

    /**
     * Dispatch onResume() to fragments.
     */
    @Override
    protected void onResume() {
        super.onResume();
        this.gameController.getState().getTimer().resumeAction();
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        this.gameController.getState().getTimer().pauseAction();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Set up the grid view for game.
     */
    private void setUpGridView() {
        GameState state = gameController.getState();
        rowNum = state.getBoard().getNumRow();
        colNum = state.getBoard().getNumCol();
        createTileViews();
        gridView = findViewById(R.id.GridView);
        String backgroundFile = "ghost_level" + state.getBoard().getLevel() + "_map";
        int backgroundId = getResources().getIdentifier(backgroundFile, "drawable", getPackageName());
        gridView.setBackgroundResource(backgroundId);
        gridView.setNumColumns(colNum);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                tileWidth = gridView.getMeasuredWidth() / colNum;
                tileHeight = gridView.getMeasuredHeight() / rowNum;
                updateDisplay();
            }
        });
    }

    /**
     * Create the image views for displaying the tiles.
     */
    private void createTileViews() {
        Board board = gameController.getState().getBoard();
        tileViews = new ArrayList<>();
        for (int row = 0; row != rowNum; row++) {
            for (int col = 0; col != colNum; col++) {
                ImageView tmp = new ImageView(this);
                tmp.setBackgroundResource(board.getTileBackground(row, col));
                this.tileViews.add(tmp);
            }
        }
    }

    /**
     * Update backgrounds of the image views.
     */
    private void updateTileViews() {
        Board board = gameController.getState().getBoard();
        int nextPos = 0;
        for (ImageView v : tileViews) {
            int row = nextPos / rowNum;
            int col = nextPos % colNum;
            v.setBackgroundResource(board.getTileBackground(row, col));
            nextPos++;
        }
    }

    /**
     * Activate undo button.
     */
    private void addUndoButtonListener() {
        Button undo = findViewById(R.id.buttonUndo);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameController.getPlayerUndoStack().size() == 0) {
                    Toast.makeText(GhostHuntGameActivity.this, "No existing move",
                            Toast.LENGTH_SHORT).show();
                }
                gameController.undo();
            }
        });
    }

    /**
     * Activate restart button.
     */
    private void addRestartButtonListener() {
        Button restart = findViewById(R.id.buttonRestart);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameController.restart();
            }
        });
    }

    /**
     * Activate direction control buttons.
     */
    private void addDirectionButtonListener() {
        ImageButton up = findViewById(R.id.UpButton);
        ImageButton down = findViewById(R.id.DownButton);
        ImageButton left = findViewById(R.id.LeftButton);
        ImageButton right = findViewById(R.id.RightButton);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameController.processEvent(Direction.UP);
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameController.processEvent(Direction.DOWN);
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameController.processEvent(Direction.LEFT);
            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameController.processEvent(Direction.RIGHT);
            }
        });
    }

    /**
     * Update tile display components and adapter.
     */
    private void updateDisplay() {
        updateTileViews();
        gridView.setAdapter(new GridViewAdapter(tileViews, tileWidth, tileHeight));
    }

    /**
     * Update the activity view when observing object changed.
     *
     * @param o   the observable object
     * @param arg an argument passed to the observer
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg == GameController.BOARD_CHANGE) {
            updateDisplay();
        } else if (arg == GameController.LEVEL_OVER) {
            setNextLevel();
            updateDisplay();
        } else if (arg == GameController.GAME_OVER) {
            quitGame();
            Toast.makeText(this, "You are dead!", Toast.LENGTH_LONG).show();
            finish();
        } else if (arg == GameController.GAME_FINISH) {
            finishGame();
        }
    }

    /**
     * Set up next level.
     */
    private void setNextLevel() {
        int level = gameController.getState().getBoard().getLevel();
        // use level instead of level + 1 because
        // next level has already been set before notify the activity
        String fileName = "ghost_level" + level + "_map";
        int id = getResources().getIdentifier(fileName, "drawable", getPackageName());
        gridView.setBackgroundResource(id);
    }

    /**
     * Interrupt game process.
     */
    private void quitGame() {
        gameController.getState().getTimer().pauseAction();
        Intent i = new Intent();
        i.putExtra(GhostHuntStartingActivity.QUIT_STATUS, GAME_QUIT);
        i.putExtra(GameState.INTENT_NAME, gameController.getState());
        setResult(RESULT_OK, i);
    }

    /**
     * Finish game process.
     */
    private void finishGame() {
        gameController.getState().getTimer().pauseAction();
        Intent i = new Intent();
        i.putExtra(GhostHuntStartingActivity.QUIT_STATUS, GAME_FINISH);
        i.putExtra(GameState.INTENT_NAME, gameController.getState());
        int move = gameController.getState().getMoveCount();
        int time = gameController.getState().getTimer().getTotalTime();
        i.putExtra("totalTime", time);
        i.putExtra("move", move);
        setResult(RESULT_OK, i);
        Toast.makeText(this, "You escaped from the ghost!", Toast.LENGTH_LONG).show();
        finish();
    }
}
