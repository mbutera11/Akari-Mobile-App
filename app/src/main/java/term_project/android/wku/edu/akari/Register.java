package term_project.android.wku.edu.akari;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Register extends AppCompatActivity {

    protected EditText firstName;
    protected EditText lastName;
    protected EditText email;
    protected EditText password;
    protected EditText passConfirm;
    protected EditText phone;

    protected Button register;
    private ProgressBar progressBar;

    private Session s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        passConfirm = findViewById(R.id.passConfirm);

        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);


    }

    public void register(View view) {

        // all edit texts are required
        if(firstName.getText().toString().equals("") || lastName.getText().toString().equals("") || email.getText().toString().equals("")
                || password.getText().toString().equals("") || passConfirm.getText().toString().equals("") || phone.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "All boxes are required", Toast.LENGTH_LONG).show();
        } else {

            String passwordText = password.getText().toString();
            String passConfirmText = passConfirm.getText().toString();

            // check to see if password and confirm password text match
            // if match, continue with register
            // if not match, toast message
            if (passwordText.equals(passConfirmText)) {
                String firstNameText = firstName.getText().toString();
                String lastNameText = lastName.getText().toString();
                String emailText = email.getText().toString();
                String phoneText = phone.getText().toString();
                new RegisterAction(this).execute(firstNameText, lastNameText, emailText, phoneText, passwordText);

            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            }
        }

    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    // subclass that executed the register script on the server via POST
    private class RegisterAction extends AsyncTask<String, Integer, String> {

        private Context context;

        public RegisterAction(Context context) {
            this.context = context;
        }

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        // communication happens here
        @Override
        protected String doInBackground(String... arg0) {
            try{

                // get input from button click
                String firstName = arg0[0];
                String lastName = arg0[1];
                String email = arg0[2];
                String phone = arg0[3];
                String password = arg0[4];

                // link to local script
                //String link = "http://10.0.1.38/Code/Mobile_App_Scripts/registerMobile.php";

                // link to script on server
                String link = "http://akari.alsolaim.com/Mobile_App_Scripts/registerMobile.php";

                // encode data to URL to pass via POST method
                String data  = URLEncoder.encode("firstName", "UTF-8") + "=" + URLEncoder.encode(firstName, "UTF-8");
                data += "&" + URLEncoder.encode("lastName", "UTF-8") + "=" + URLEncoder.encode(lastName, "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");
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

            progressBar.setVisibility(View.INVISIBLE);
            String[] allInfo = result.split(",");

            // if first element in array is an exception, no internet connection toast message
            if(allInfo[0].equals("Exception: Unable to resolve host \"akari.alsolaim.com\": No address associated with hostname")) {
                Toast.makeText(getApplicationContext(), "No Connection. Check internet connection", Toast.LENGTH_LONG).show();
            } else {
                s = new Session(getApplicationContext());
                s.setFirstName(allInfo[0]);
                s.setLastName(allInfo[1]);
                s.setEmail(allInfo[3]);

                Intent in = new Intent(getApplicationContext(), Profile.class);
                startActivity(in);
            }

        }
    }
}
