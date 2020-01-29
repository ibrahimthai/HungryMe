package pt.ismai.hungryme.AccountRecovery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Random;

import pt.ismai.hungryme.LoginAndRegister.DbHelper;
import pt.ismai.hungryme.LoginAndRegister.LoginActivity;
import pt.ismai.hungryme.LoginAndRegister.Session;
import pt.ismai.hungryme.R;


public class InputEmailActivity extends AppCompatActivity implements View.OnClickListener {

    private Button submitButton;
    private TextView iRememberPassword;
    private EditText inputEmail;

    public static final String MyPREFERENCES = "MyPrefs" ;
    private DbHelper db;
    private Session session;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_email);

        submitButton = (Button)findViewById(R.id.submitEmailButton);
        submitButton.setOnClickListener(this);

        // If you remember your password
        iRememberPassword = (TextView)findViewById(R.id.rememberPasswordLink);
        iRememberPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputEmailActivity.this, LoginActivity.class));
                finish();
            }
        });

        inputEmail = (EditText)findViewById(R.id.userEmail);
        db = new DbHelper(this);
        session = new Session(this);


        } // End of OnCreate

    protected void sendEmail(String userEmail) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Random rand = new Random();
        String randomCode = String.format("%04d", rand.nextInt(10000));


        if(db.getEmail(userEmail)){


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

            Log.d("","http://nick.macedo.ca/email.php?email=" + inputEmail + "&message=" + randomCode,new Throwable());
            System.out.println("BEFORE: http://nick.macedo.ca/email.php?email=" + userEmail + "&message=" + randomCode);


            URL url;
            StringBuffer response = new StringBuffer();
            try {
                url = new URL("http://nick.macedo.ca/email.php?email=" + userEmail + "&message=" + randomCode);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("invalid url");
            }

            HttpURLConnection conn = null;
            try {
                System.out.println("HITTING: http://nick.macedo.ca/email.php?email=" + userEmail + "&message=" + randomCode);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(false);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");


                // handle the response
                int status = conn.getResponseCode();
                if (status != 200) {
                    throw new IOException("Post failed with error code " + status);
                } else {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("CATCH: http://nick.macedo.ca/email.php?email=" + userEmail + "&message=" + randomCode);
            } finally {
                System.out.println("AFTER: http://nick.macedo.ca/email.php?email=" + userEmail + "&message=" + randomCode);
                if (conn != null) {
                    conn.disconnect();
                }

                //Here is your json in string format
                String responseJSON = response.toString();
            }




            // 1. PASS email to InputCodeActivity
            Intent intent = new Intent(this, InputCodeActivity.class);
            intent.putExtra("UsersEmail", userEmail);
            intent.putExtra("UsersCode", randomCode);
            startActivity(intent);

            Toast.makeText(getApplicationContext(), "CODE: "+randomCode,Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(getApplicationContext(), "Email does not exist",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        // Users email address
        String email = fetchUserEmail();
        switch(v.getId()){
            case R.id.submitEmailButton:
            {
                sendEmail(email);
            }
                break;
            default:
        }
    }


    // Hold the users email for this class
    public String fetchUserEmail(){
        String fetchedEmail = inputEmail.getText().toString();
        return fetchedEmail;
    }


} // End of InputEmailActivity
