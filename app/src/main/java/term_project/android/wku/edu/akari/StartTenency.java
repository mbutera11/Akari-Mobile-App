package term_project.android.wku.edu.akari;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_tenency);

        s = new Session(this);
        String userEmail = s.getEmail();

        radioGroup = findViewById(R.id.propertyGroup);

    }

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

            for(int i = 0; i < strings.size(); i++) {
                String address = strings.get(i);

                RadioButton radioButton = new RadioButton(getApplicationContext());
                radioButton.setId(i);
                radioButton.setText(address);
                radioGroup.addView(radioButton);
            }

        }
    }




}
