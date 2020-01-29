package pt.ismai.hungryme.AccountRecovery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pt.ismai.hungryme.LoginAndRegister.DbHelper;
import pt.ismai.hungryme.LoginAndRegister.LoginActivity;
import pt.ismai.hungryme.LoginAndRegister.Session;
import pt.ismai.hungryme.R;

public class InputNewPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView cancel;
    private EditText inputNewPassword;
    private EditText inputConfirmedPassword;
    private Button changePasswordButton;

    public static final String MyPREFERENCES = "MyPrefs" ;
    private DbHelper db;
    private Session session;
    private InputEmailActivity fetchUsersEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_new_password);

        changePasswordButton = (Button)findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(this);


        // If you want to cancel changing your password
        cancel = (TextView)findViewById(R.id.cancelLink);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputNewPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });

        inputNewPassword = (EditText)findViewById(R.id.newUserPassword);
        inputConfirmedPassword = (EditText)findViewById(R.id.confirmUserPassword);
        db = new DbHelper(this);
        session = new Session(this);


    } // End of OnCreate


    protected void editUsersPassword() {

        String newPassword = inputNewPassword.getText().toString();
        String confirmedPassword = inputConfirmedPassword.getText().toString();

        // 3. Now GET email
        Bundle b = getIntent().getExtras();
        String userEmail = b.getString("UsersEmail");

        if ( !(newPassword.equals(confirmedPassword)) )
        {
            Toast.makeText(getApplicationContext(), "New password and confirmed password is incorrect",Toast.LENGTH_LONG).show();
        }
        else if ( newPassword.isEmpty() || confirmedPassword.isEmpty() )
        {
            Toast.makeText(getApplicationContext(), "No passwords are inputted. Please try again",Toast.LENGTH_LONG).show();
        }
        // If new password isn't strong enough (at least 8 characters and includes either a number or special character)
        else if (newPassword.length() < 8 || newPassword.matches("[a-zA-Z]+")) {
            Toast.makeText(getApplicationContext(), "Passwords must be at least 8 characters long and contain a number and/or special character", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), newPassword,Toast.LENGTH_SHORT).show();
            db.changePassword(userEmail, newPassword);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.changePasswordButton:
                editUsersPassword();
                break;
            default:
        }
    }







} // End of InputNewPasswordActivity
