package pt.ismai.hungryme.AccountRecovery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pt.ismai.hungryme.R;

public class InputCodeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView wrongEmail;
    private Button submitCodeButton;
    private EditText fourDigitCodeInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_code);

        submitCodeButton = (Button)findViewById(R.id.submitFourDigitButton);
        submitCodeButton.setOnClickListener(this);

        fourDigitCodeInput = (EditText)findViewById(R.id.userFourDigitCode);

        // If you remember your password
        wrongEmail = (TextView)findViewById(R.id.wrongEmailLink);
        wrongEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputCodeActivity.this, InputEmailActivity.class));
                finish();
            }
        });

    } // End of OnCreate


    protected void scanFourDigitCode() {

        String codeInput = fourDigitCodeInput.getText().toString();

        Bundle b = getIntent().getExtras();
        String userEmail = b.getString("UsersEmail");
        String getCode = b.getString("UsersCode");

        // Checks if the code is correct or not
        if ( codeInput.isEmpty() ) {
            Toast.makeText(getApplicationContext(), "No code has been inputted. Please try again.",Toast.LENGTH_SHORT).show();
        }
        else if( !(codeInput.equals(getCode)) ) {
            Toast.makeText(getApplicationContext(), "Code input is incorrect. Please try again.",Toast.LENGTH_SHORT).show();
        }
        else {

            // 2. Now PASS email to InputNewPasswordActivity
            Intent intent = new Intent(this, InputNewPasswordActivity.class);
            intent.putExtra("UsersEmail", userEmail);
            startActivity(intent);

            Toast.makeText(getApplicationContext(), userEmail,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.submitFourDigitButton:
                scanFourDigitCode();
                break;
            default:
        }
    }














} // End of InputCodeActivity
