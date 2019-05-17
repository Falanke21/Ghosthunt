package fall2018.csc2017.game_centre.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fall2018.csc2017.game_centre.R;

/**
 * View class, excluded from unit test.
 * The initial activity for the sliding puzzle tile game.
 */
public class SlidingTilesStartingActivity extends AppCompatActivity {

    /**
     * The file saver for file io.
     */
    private SlidingTilesFileHandler fileSaver;

    /**
     * Current player's gameState.
     */
    private GameState gameState;

    /**
     * On create method
     * @param savedInstanceState param need from superclass.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileSaver = SlidingTilesFileHandler.getInstance();
        fileSaver.loadFromFile(this);
        gameState = fileSaver.getGameState();
        setContentView(R.layout.activity_slidingtiles_starting);
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();
        addScoreboardListener();
    }

    /**
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SlidingTilesStartingActivity.this);
                View inflater = getLayoutInflater().inflate(R.layout.dialog_slidingtiles_starting, null);
                builder.setView(inflater);
                AlertDialog dialog = builder.create();
                setUpDialog(inflater, dialog);
                dialog.show();
            }
        });
    }

    /**
     * Set up game starting info dialog.
     * @param view the view holding dialog
     * @param dialog the dialog to set up
     */
    private void setUpDialog(final View view, final AlertDialog dialog) {
        Button start = view.findViewById(R.id.DialogStartButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText dimension = view.findViewById(R.id.Dimension);
                    EditText undo = view.findViewById(R.id.Undo);
                    int dim = Integer.parseInt(dimension.getText().toString());
                    int u = Integer.parseInt(undo.getText().toString());
                    if (dim >= 3 && dim <= 5 && u >= 0) {
                        gameState = new GameState(u, dim);
                        fileSaver.setGameState(gameState);
                        switchToGame();
                        dialog.dismiss();
                    } else {
                        makeToastText("Invalid input");
                    }
                } catch (NumberFormatException e) {
                    makeToastText("Fill in empty field");
                }
            }
        });
    }

    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileSaver.loadFromFile(SlidingTilesStartingActivity.this
                );

                if (gameState == null) {
                    makeToastText("No previous saved game");
                } else {
                    makeToastText("Loaded Game");
                    switchToGame();
                }
            }
        });
    }

    /**
     * Activate the save button.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fileSaver.saveToFile(SlidingTilesStartingActivity.this
                );
                makeToastText("Game Saved");
            }
            });
    }

    /**
     * Display message in sliding tiles starting activity.
     * @param msg the message to display
     */
    private void makeToastText(String msg) {
        Toast.makeText(SlidingTilesStartingActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Activate Scoreboard Button.
     */
    private void addScoreboardListener() {
        Button scoreboard = findViewById(R.id.Scoreboard);
        scoreboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToScoreboard();
            }
        });
    }

    /**
     * Switch to the SlidingTilesGameActivity view to play the game.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, SlidingTilesGameActivity.class);
        startActivity(tmp);
    }

    /**
     * Switch to the Scoreboard of Sliding Tiles.
     */
    private void switchToScoreboard() {
        Intent tmp = new Intent(this, SlidingTilesScoreBoardActivity.class);
        startActivity(tmp);
    }
}
