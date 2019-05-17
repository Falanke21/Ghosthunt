package fall2018.csc2017.game_centre;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import fall2018.csc2017.game_centre.ghost_hunt.GhostHuntStartingActivity;
import fall2018.csc2017.game_centre.slidingtiles.SlidingTilesStartingActivity;
import fall2018.csc2017.game_centre.sudoku.SudokuStartingActivity;

/**
 * View class, excluded from unit test.
 * Game selection centre activity.
 */
public class GameCentreActivity extends AppCompatActivity {

    /**
     * Currently available games.
     */
    private final Map<Game, Class> GAMES = new HashMap<>();
    {
        GAMES.put(Game.SlidingTiles, SlidingTilesStartingActivity.class);
        GAMES.put(Game.Sudoku, SudokuStartingActivity.class);
        GAMES.put(Game.GhostHunt, GhostHuntStartingActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_centre);
        addSlidingTilesButtonListener();
        addSudokuButtonListener();
        addGhostHuntButtonListener();
        addProfileButtonListener();
        Toast.makeText(this,
                String.format("Welcome back, %s", CurrentStatus.getCurrentUser().getUsername()),
                Toast.LENGTH_LONG).show();
    }


    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        switchToLogin();
    }

    /**
     * Activate SlidingTiles button.
     */
    private void addSlidingTilesButtonListener() {
        Button slidingTiles = findViewById(R.id.SlidingTiles);
        slidingTiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToGame(Game.SlidingTiles);
            }
        });
    }

    /**
     * Activate Sudoku button.
     */
    private void addSudokuButtonListener() {
        Button sudoku = findViewById(R.id.Sudoku);
        sudoku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGame(Game.Sudoku);
            }
        });
    }

    /**
     * Activate GhostHunt Button.
     */
    private void addGhostHuntButtonListener() {
        Button ghostHunt = findViewById(R.id.GhostHunt);
        ghostHunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGame(Game.GhostHunt);
            }
        });
    }

    /**
     * Activate Profile Button.
     */
    private void addProfileButtonListener(){
        Button userCentre = findViewById(R.id.userCentre);
        userCentre.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * Switch to login activity.
     */
    private void switchToLogin() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    /**
     * Switch to corresponding game activity based on input index.
     * @param game name of the game in Game enum
     */
    private void switchToGame(Game game) {
        Intent tmp = new Intent(this, GAMES.get(game));
        startActivity(tmp);
    }
}
