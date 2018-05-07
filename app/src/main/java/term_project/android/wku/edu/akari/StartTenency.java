// Michael Butera
// Tom Spencer

package term_project.android.wku.edu.akari;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class StartTenency extends AppCompatActivity {

    protected RadioGroup radioGroup;
    protected Session s;

    protected EditText renterEmail;
    protected EditText contractDate;

    protected Button rental;

    protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tenency);
        getSupportActionBar().setTitle("Start Tenancy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        s = new Session(this);
        String userEmail = s.getEmail();

        radioGroup = findViewById(R.id.rGroup);
        renterEmail = findViewById(R.id.renterEmail);
        contractDate = findViewById(R.id.contractDate);
        progressBar = findViewById(R.id.progressBar2);

        // call asynctask, this populates radio group with properties the user owns
        new GetPropertyTenency().execute(userEmail);

        rental = findViewById(R.id.rental);

        // when rental button is clicked, start rental process on server
        rental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioID = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = findViewById(radioID);
                // make sure all inputs have been written
                if(renterEmail.getText().toString().equals("") || contractDate.getText().toString().equals("") || radioID == -1) {
                    Toast.makeText(getApplicationContext(), "All Fields are Required", Toast.LENGTH_LONG).show();
                } else if(rb.getText().equals("There are no properties")) {
                    Toast.makeText(getApplicationContext(), "You Have No Properties to Rent", Toast.LENGTH_LONG).show();
                } else {
                    new StartTenencyAction().execute(s.getEmail(), renterEmail.getText().toString(), contractDate.getText().toString(), (String) rb.getText());
                }
            }
        });

    }

    // async class to populate the radio group with properties that the user owns
    private class GetPropertyTenency extends AsyncTask<String, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            try {

                String email = strings[0];

                // link to script on server
                String link = "http://akari.alsolaim.com/Mobile_App_Scripts/getPropertyTenencyMobile.php";

                // encode the email and password to pass to the script via POSt
                String data  = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");

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

                // will hold all properties on server
                ArrayList<String> list = new ArrayList<String>();

                String line;
                // Read Server Response and add to array list
                while((line = reader.readLine()) != null) {
                    list.add(line);
                }

                return list;


            } catch(Exception e) {
                Log.d("ERROR", "Exception: " + e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {

            // get response from server, split it
            String addresses = strings.get(0);

            String[] seperate = addresses.split(":");

            // radio buttons that will be added to page
            RadioButton[] rbuttons = new RadioButton[seperate.length];

            // loop through number of properties returned from server, add radio for each one
            for(int i = 0; i < seperate.length; i++) {
                String address = seperate[i];
                rbuttons[i] = new RadioButton(getApplicationContext());
                rbuttons[i].setText(address);
                rbuttons[i].setTextColor(Color.BLACK);
                rbuttons[i].setId(i + 100);
                Log.d("ADDRESS", "Address: " + address);
                radioGroup.addView(rbuttons[i]);
            }

        }
    }

    // async class to do the actual rental functionality
    private class StartTenencyAction extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            rental.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                // items to send to server
                String landlordEmail = strings[0];
                String renterEmail = strings[1];
                String date = strings[2];
                String address = strings[3];

                // link to script on server
                String link = "http://akari.alsolaim.com/Mobile_App_Scripts/startTenencyMobile.php";

                // encode the email and password to pass to the script via POSt
                String data  = URLEncoder.encode("landlordEmail", "UTF-8") + "=" + URLEncoder.encode(landlordEmail, "UTF-8");
                data += "&" + URLEncoder.encode("renterEmail", "UTF-8") + "=" + URLEncoder.encode(renterEmail, "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                data += "&" + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(address, "UTF-8");

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

                String line;
                // Read Server Response and add to array list
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();


            } catch (Exception e) {
                Log.d("ERROR", "Exception: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {

            // cases depending on server output
            if(s.equals("SUCCESS")) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Rental was Successful", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(getApplicationContext(), Profile.class));
            } else if(s.equals("FAIL")) {
                progressBar.setVisibility(View.INVISIBLE);
                rental.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Something went wrong ... ", Toast.LENGTH_LONG).show();
            } else if(s.equals("NO RENTER")) {
                progressBar.setVisibility(View.INVISIBLE);
                rental.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "There is no valid user with that email", Toast.LENGTH_LONG).show();
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                rental.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
