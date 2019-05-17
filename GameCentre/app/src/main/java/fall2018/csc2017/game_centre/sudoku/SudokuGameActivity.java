package fall2018.csc2017.game_centre.sudoku;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import fall2018.csc2017.game_centre.CurrentStatus;
import fall2018.csc2017.game_centre.R;

/**
 * View class, excluded from unit test.
 *
 * Game activity for sudoku.
 */
public class SudokuGameActivity extends AppCompatActivity implements Observer {

    /**
     * The game state of current sudoku game.
     */
    private SudokuGameState gameState;

    /**
     * The file handler for sudoku.
     */
    private SudokuFileHandler fileHandler = SudokuFileHandler.getInstance();

    /**
     * SudokuBoard view for the SudokuBoard cells.
     */
    private SudokuGridView gridView;

    /**
     * Sudoku game controller.
     */
    private SudokuGameController gameController;

    /**
     * Buttons to display.
     */
    private ArrayList<Button> cellButtons;

    /**
     * SudokuBoard cell dimensions.
     */
    private int cellWidth;
    private int cellHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameState = fileHandler.getGameState();
        createCellButtons(this);
        setContentView(R.layout.activity_sudoku_game);
        setUpGridView();

        gameController = gridView.getGameController();
        gameController.setGameState(gameState);
        gameController.addObserver(this);
        gameState.addObserver(this);
        // Add button listeners
        for (int i = 1; i < 10; i++) {
            addAnswerButtonListener(i);
        }
        addHintButtonListener();
        displayPlayerInformation();
    }

    /**
     * Create grid buttons. Also sets up the background ids for every cell.
     * @param context where button is displayed
     */
    private void createCellButtons(Context context) {
        SudokuBoard board = gameState.getBoard();
        cellButtons = new ArrayList<>();
        for (int row = 0; row != board.getSideLen(); row++) {
            for (int col = 0; col != board.getSideLen(); col++) {
                Button tmp = new Button(context);

                // Set up background ids from R to Cells.
                Cell cell = board.getCell(row, col);
                setBackgroundIdFromR(cell);

                tmp.setBackgroundResource(board.getCell(row, col).getBackground());
                this.cellButtons.add(tmp);
            }
        }
    }

    /**
     * use getResources method to find cell background id from R. Store it into the cell.
     * @param cell    the cell that is generating background id
     */
    private void setBackgroundIdFromR(Cell cell){
        int cellValue = cell.getValue();
        // Set number background
        String resource = "sudoku_" + Integer.toString(cellValue) + "a";
        cell.setNumberBackground(this.getResources().getIdentifier(
                resource, "drawable", getPackageName()));

        // Set colored background
        resource = "sudoku_" + Integer.toString(cellValue) + "a_coloured";
        cell.setColoredBackground(this.getResources().getIdentifier(
                resource, "drawable", getPackageName()));
    }

    /**
     * Helper function for setting up the grid view in activity.
     */
    private void setUpGridView() {
        gridView = findViewById(R.id.sudokuGridView);
        gridView.setNumColumns(SudokuBoard.SIDE_LEN);

        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();
                        cellWidth = displayWidth / SudokuBoard.SIDE_LEN;
                        cellHeight = displayHeight / SudokuBoard.SIDE_LEN;
                        display();
                    }
                });
    }

    /**
     * Display all cells in grid view.
     */
    private void display() {
        updateTileButtons();
        gridView.setAdapter(new SudokuGridViewAdapter(cellButtons, cellWidth, cellHeight));

    }

    /**
     * Update display only for cells in array list cells.
     * @param cells the cells that need to be update display.
     */
    private void display(ArrayList<Cell> cells){
        updateTileButtons(cells);
        gridView.setAdapter(new SudokuGridViewAdapter(cellButtons, cellWidth, cellHeight));
    }

    /**
     * Update display only for cells in array list cells.
     * @param cells the cells that need to be update display.
     */
    private void updateTileButtons(ArrayList<Cell> cells){
        for (Cell cell:cells) {
            cellButtons.get(cell.getPosition()).setBackgroundResource(cell.getBackground());
        }
    }

    /**
     * Update every cell with changing the background of buttons.
     */
    private void updateTileButtons() {
        SudokuBoard board = gameState.getBoard();
        int nextPos = 0;
        for (Button b : cellButtons) {
            int row = nextPos / SudokuBoard.SIDE_LEN;
            int col = nextPos % SudokuBoard.SIDE_LEN;
            b.setBackgroundResource(board.getCell(row, col).getBackground());
            nextPos++;
        }
    }

    /**
     * activate the 9 answer buttons.
     * @param buttonNum the num that is appeared on the button.
     */
    private void addAnswerButtonListener(final int buttonNum) {
        String resource = "AnswerButton" + Integer.toString(buttonNum);
        int buttonId = this.getResources().getIdentifier(resource, "id", getPackageName());
        Button answerButton = findViewById(buttonId);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameController.answerButtonClicked(SudokuGameActivity.this, buttonNum);
                displayMistakes();
            }
        });
    }

    /**
     * activate the hint button
     */
    private void addHintButtonListener() {
        Button hint = findViewById(R.id.buttonHint);
        hint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameController.hint(SudokuGameActivity.this);
                displayHintCount();
            }
        });
    }

    /**
     * This method is called when the Activity just got created.
     */
    private void displayPlayerInformation(){
        displayUsername();
        displayHintCount();
        displayMistakes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameState.getTimer().resumeAction();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameState.getTimer().pauseAction();
    }

    /**
     * Display message in sliding tiles starting activity.
     * @param msg the message to display
     */
    private void makeToastText(String msg) {
        Toast.makeText(SudokuGameActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof SudokuGameState){
            makeToastText("You Were Wrong Four Times YOU SUCKA");
            finish();
        }
        else if (o instanceof SudokuGameController){
            if (arg == null){
                display();
            }
            else if (arg instanceof ArrayList){
                display((ArrayList<Cell>) arg);
            }
            else if (arg instanceof String){
                makeToastText((String)arg);
            }
            else {
                makeToastText("Sudoku Solved");
                fileHandler.saveToFile(this);
                switchToScoreBoard((int[])arg);
            }
        }
    }

    /**
     * The method that gets called when the game finishes. And game switches to scoreboard.
     * @param information the information that scoreboard needed for calculating scores.
     */
    private void switchToScoreBoard(int[] information){
        Intent i = new Intent(this, SudokuScoreBoardActivity.class);
        switch (gameState.getDifficulty()) {
            case "easy":
                i.putExtra("totalTime", information[0] * 3);
                break;
            case "medium":
                i.putExtra("totalTime", information[0] * 2);
                break;
            default:
                i.putExtra("totalTime", information[0]);
                break;
        }
        i.putExtra("hintCounter", information[1]);
        i.putExtra("wrongCounter", information[2]);
        startActivity(i);
        finish();
    }

    /**
     * set up hint counter.
     */
    private void displayHintCount() {
        String result = String.valueOf(gameState.getHintCounter());
        ((TextView)findViewById(R.id.Hints)).setText(result);
    }

    /**
     * set up username display.
     */
    private void displayUsername() {
        ((TextView)findViewById(R.id.player_textview)).setText(
                CurrentStatus.getCurrentUser().getUsername());
    }

    /**
     * set up display mistakes.
     */
    private void displayMistakes(){
        String result = String.valueOf(gameState.getWrongCounter());
        ((TextView)findViewById(R.id.mistakeTime)).setText(result);
    }
}
