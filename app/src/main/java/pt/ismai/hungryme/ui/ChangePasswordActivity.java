package pt.ismai.hungryme.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import pt.ismai.hungryme.LoginAndRegister.DbHelper;
import pt.ismai.hungryme.R;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 ChangePasswordActivity allows the user to change their password
 The user gets here by clicking the "Change Password" button on the Account page
 */
public class ChangePasswordActivity extends AppCompatActivity {

    private Button btnChangePassword;
    private ImageButton btnBack;
    private EditText oldPassword, newPassword;
    private DbHelper db;
    public static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences prefs;
    /**
     * on create sets up the layout and the action listeners
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        db = new DbHelper(this);
        prefs = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        oldPassword = (EditText) findViewById(R.id.oldPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);

        addListenerOnButton();
        addListenerOnBackButton();
    }

    /**
     * Adds listener to Change Password Button (Confirm button)
     */
    public void addListenerOnButton() {
        btnChangePassword = (Button) findViewById(R.id.btnChangePasswordConfirm);
        btnChangePassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = newPassword.getText().toString();
                String enteredOldPassword = oldPassword.getText().toString();
                String email = prefs.getString("EMAIL", "");
                // If old password and new password are the same
                if (enteredOldPassword.equals(enteredPassword)) {
                    Toast.makeText(ChangePasswordActivity.this, "Old Password and New Password can't be the same!", Toast.LENGTH_LONG).show();
                }
                // If old password isn't correct
                else if (!db.getUser(email,enteredOldPassword))
                {
                    Toast.makeText(ChangePasswordActivity.this, "Old Password entered is incorrect!", Toast.LENGTH_LONG).show();
                }
                // If new password isn't strong enough (at least 8 characters and includes either a number or special character)
                else if (enteredPassword.length() < 8 || enteredPassword.matches("[a-zA-Z]+"))
                {
                    Toast.makeText(ChangePasswordActivity.this, "Passwords must be at least 8 characters long and contain a number and/or special character", Toast.LENGTH_LONG).show();
                }
                // Valid change
                else
                {
                    Toast.makeText(ChangePasswordActivity.this, "Successfully changed password", Toast.LENGTH_SHORT).show();
                    db.changePassword(email, enteredPassword);
                    finish();
                }

            }
        });
    }

    /**
     * Adds listener to the back button on the screen
     */
    public void addListenerOnBackButton() {
        btnBack = (ImageButton) findViewById(R.id.backButton);
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
