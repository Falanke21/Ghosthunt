package fall2018.csc2017.game_centre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import static fall2018.csc2017.game_centre.R.layout.activity_login;


/**
 * View class, exclude from tests.
 * The login in page activity.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * The fileHandler of Users.
     */
    private UserFileHandler fileHandler = UserFileHandler.getInstance();

    /**
     * Mapping from username to user.
     */
    private Map<String, User> users;

    /**
     * The on create method. Load previous users info from file.
     *
     * @param savedInstanceState parameter from superclass.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_login);

        fileHandler.loadFromFile(this);
        this.users = fileHandler.getUsers();

        addLoginButtonListener();
        addSignUpLinkListener();
    }

    /**
     * Activate login button.
     */
    private void addLoginButtonListener() {
        Button login = findViewById(R.id.buttonlogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etUsername = findViewById(R.id.etUsername);
                EditText etPassword = findViewById(R.id.etPassword);
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (users.containsKey(username) && password.equals(users.get(username).getPassword())) {
                    fileHandler.saveToFile(LoginActivity.this);
                    CurrentStatus.setCurrentUser(users.get(username));
                    Intent i = new Intent(getApplicationContext(), GameCentreActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Activate sign up button.
     */
    private void addSignUpLinkListener() {
        TextView signUp = findViewById(R.id.signuplink);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }
}
