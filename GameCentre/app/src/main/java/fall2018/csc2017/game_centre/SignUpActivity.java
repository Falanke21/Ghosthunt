package fall2018.csc2017.game_centre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;
import java.util.regex.*;

/**
 * View class, exclude from test.
 * The activity of the sign up page.
 */
public class SignUpActivity extends AppCompatActivity {
    /**
     * The file handler for user file io.
     */
    private UserFileHandler fileHandler = UserFileHandler.getInstance();

    /**
     * Mapping from username to user.
     */
    private Map<String, User> users;

    /**
     * The on create method for init
     *
     * @param savedInstanceState activity field needed by superclass
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fileHandler.loadFromFile(this);
        users = fileHandler.getUsers();

        addSignUpButtonListener();
    }

    /**
     * Activate sign up button.
     */
    private void addSignUpButtonListener() {
        Button signUp = findViewById(R.id.buttonsignup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etUsername = findViewById(R.id.etUsername);
                EditText etPassword = findViewById(R.id.etPassword);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (Pattern.matches("\\w*", username)) {
                    if (username.isEmpty() || password.isEmpty()) {
                        makeToastText("Fill in empty field");
                    } else if (users.containsKey(username)) {
                        makeToastText("User already exists");
                    } else {
                        users.put(username, new User(username, password));
                        makeToastText("Sign Up successful");
                        switchToLogin();
                    }
                } else makeToastText("Invalid Username, Please Enter Only Letters Or UnderScore.");
            }
        });
    }

    /**
     * Switch to login activity after successful sign up.
     */
    private void switchToLogin() {
        fileHandler.saveToFile(this);
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    /**
     * Make text using Toast.
     *
     * @param msg message to display
     */
    private void makeToastText(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
