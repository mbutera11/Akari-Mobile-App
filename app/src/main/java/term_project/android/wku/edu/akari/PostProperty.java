package term_project.android.wku.edu.akari;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class PostProperty extends AppCompatActivity {

    protected EditText propName;
    protected EditText country;
    protected EditText street;
    protected EditText city;
    protected EditText state;
    protected EditText zip;
    protected EditText size;
    protected EditText numBed;
    protected EditText numBath;
    protected EditText description;
    protected EditText price;
    protected EditText availability;
    protected EditText construction;
    protected EditText problems;

    protected RadioGroup propType;
    protected RadioGroup leaseTerm;

    protected Button registerProperty;

    Session s;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_property);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        propName = findViewById(R.id.propertyName);
        country = findViewById(R.id.country);
        street = findViewById(R.id.street);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        zip = findViewById(R.id.zip);
        size = findViewById(R.id.size);
        numBed = findViewById(R.id.numBed);
        numBath = findViewById(R.id.numBath);
        description = findViewById(R.id.description);
        price = findViewById(R.id.price);
        availability = findViewById(R.id.available);
        construction = findViewById(R.id.construction);
        problems = findViewById(R.id.problems);

        propType = findViewById(R.id.propertyType);
        leaseTerm = findViewById(R.id.leaseTerm);

        registerProperty = findViewById(R.id.registerProperty);

        s = new Session(getApplicationContext());


        // execute appropriate tasks when butotn is clicked
        registerProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int propTypeID = propType.getCheckedRadioButtonId();
                int leaseTermID = leaseTerm.getCheckedRadioButtonId();

                if(propName.getText().toString().equals("") ||  country.getText().toString().equals("") || street.getText().toString().equals("") || city.getText().toString().equals("") ||
                        state.getText().toString().equals("") || zip.getText().toString().equals("") || size.getText().toString().equals("") || numBed.getText().toString().equals("") || numBath.getText().toString().equals("") ||
                        description.getText().toString().equals("") || price.getText().toString().equals("") || availability.getText().toString().equals("") || construction.getText().toString().equals("") ||
                        problems.getText().toString().equals("") || propTypeID == -1 || leaseTermID == -1) {

                    Toast.makeText(getApplicationContext(), "All Fields are Required", Toast.LENGTH_LONG).show();

                } else {

                    // get checked radio buttons text
                    RadioButton rb1 = findViewById(propTypeID);
                    String propTypeText = rb1.getText().toString();
                    RadioButton rb2 = findViewById(leaseTermID);
                    String leaseTermText = rb2.getText().toString();

                    // call async task and pass all strings
                    new RegisterPropertyAction().execute(propName.getText().toString(), country.getText().toString(), street.getText().toString(), city.getText().toString(),
                            state.getText().toString(), zip.getText().toString(), size.getText().toString(), numBed.getText().toString(), numBath.getText().toString(),
                            description.getText().toString(), price.getText().toString(), availability.getText().toString(), construction.getText().toString(),
                            problems.getText().toString(), propTypeText, leaseTermText);

                }
            }
        });

    }


    private class RegisterPropertyAction extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {


            try {
                String propName = strings[0];
                String country = strings[1];
                String street = strings[2];
                String city = strings[3];
                String state = strings[4];
                String zip = strings[5];
                String size = strings[6];
                String numBed = strings[7];
                String numBath = strings[8];
                String description = strings[9];
                String price = strings[10];
                String availability = strings[11];
                String construction = strings[12];
                String problems = strings[13];
                String propType = strings[14];
                String leaseTerm = strings[15];


                String email = s.getEmail();

                // link to script on server
                String link = "http://akari.alsolaim.com/Mobile_App_Scripts/registerPropertyMobile.php";

                // encode data to URL to pass via POST method
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                data += "&" + URLEncoder.encode("propName", "UTF-8") + "=" + URLEncoder.encode(propName, "UTF-8");
                data += "&" + URLEncoder.encode("country", "UTF-8") + "=" + URLEncoder.encode(country, "UTF-8");
                data += "&" + URLEncoder.encode("street", "UTF-8") + "=" + URLEncoder.encode(street, "UTF-8");
                data += "&" + URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode(city, "UTF-8");
                data += "&" + URLEncoder.encode("state", "UTF-8") + "=" + URLEncoder.encode(state, "UTF-8");
                data += "&" + URLEncoder.encode("zip", "UTF-8") + "=" + URLEncoder.encode(zip, "UTF-8");
                data += "&" + URLEncoder.encode("size", "UTF-8") + "=" + URLEncoder.encode(size, "UTF-8");
                data += "&" + URLEncoder.encode("numBed", "UTF-8") + "=" + URLEncoder.encode(numBed, "UTF-8");
                data += "&" + URLEncoder.encode("numBath", "UTF-8") + "=" + URLEncoder.encode(numBath, "UTF-8");
                data += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(description, "UTF-8");
                data += "&" + URLEncoder.encode("price", "UTF-8") + "=" + URLEncoder.encode(price, "UTF-8");
                data += "&" + URLEncoder.encode("availability", "UTF-8") + "=" + URLEncoder.encode(availability, "UTF-8");
                data += "&" + URLEncoder.encode("construction", "UTF-8") + "=" + URLEncoder.encode(construction, "UTF-8");
                data += "&" + URLEncoder.encode("problems", "UTF-8") + "=" + URLEncoder.encode(problems, "UTF-8");
                data += "&" + URLEncoder.encode("propType", "UTF-8") + "=" + URLEncoder.encode(propType, "UTF-8");
                data += "&" + URLEncoder.encode("leaseTerm", "UTF-8") + "=" + URLEncoder.encode(leaseTerm, "UTF-8");


                // create a URL with the link
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                // create writer to write encoded data to the URL
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
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

            } catch(Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String s) {

            if(s.equals("FAIL")) {
                Toast.makeText(getApplicationContext(), "Property was not inserted. Try again.", Toast.LENGTH_LONG).show();
            } else if(s.equals("Failed to connect")) {
                Toast.makeText(getApplicationContext(), "Unable to connect to database", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Property was inserted. Add a picture", Toast.LENGTH_LONG).show();
                Intent in = new Intent(getApplicationContext(), AddPictureActivity.class);
                String[] propUserID = s.split(",");

                // send user and property id from server to next activity
                in.putExtra("userID", propUserID[0]);
                in.putExtra("propertyID", propUserID[1]);

                startActivity(in);            }
        }
    }




    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
