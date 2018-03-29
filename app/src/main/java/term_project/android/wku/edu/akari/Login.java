package term_project.android.wku.edu.akari;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Login extends AppCompatActivity {

    protected EditText email;
    protected EditText password;
    protected Button loginButton;
    protected TextView firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

    }

    // method called on button click to log in
    public void login(View view) {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        // new instance of the subclass, execute it
        new LoginAction(this).execute(emailText, passwordText);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    // subclass that executed the login script on the server via POST
    private class LoginAction extends AsyncTask<String, Integer, String > {

        private Context context;

        public LoginAction(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
        }

        // communication happens here
        @Override
        protected String doInBackground(String... arg0) {
            try{
                // get email and password from when it is executed in the login method
                String email = arg0[0];
                String password = arg0[1];

                // link to the script
                String link="http://10.0.1.38/Code/Mobile_App_Scripts/loginMobile.php";

                // encode the email and password to pass to the script via POSt
                String data  = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                // create a URL with the link
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                // create writer to write encoded data to the URL
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write( data );
                wr.flush();

                // will read information received from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response and append to string builder
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                // return string
                return sb.toString();

            } catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        // called after background task is complete
        // will take appropriate actions depending on the server response
        @Override
        protected void onPostExecute(String result) {
            // toast email not registered if that is the script output
            if(result.equals("Email is not Registered")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            // toast password incorrect if that is the script output
            } else if(result.equals("Password is Incorrect")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

            // if email is registered and password is correct, login is successful
            // send user to profile page
            } else {
                Intent in = new Intent(getApplicationContext(), Profile.class);
                in.putExtra("FirstName", result);
                startActivity(in);
            }
        }
    }

}
