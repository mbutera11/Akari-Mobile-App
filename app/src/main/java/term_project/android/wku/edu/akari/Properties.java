// Michael Butera
// Tom Spencer

package term_project.android.wku.edu.akari;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Properties extends AppCompatActivity {

    protected String[] imageArray;

    protected String[] priceArray;
    protected String[] numBedArray;
    protected String[] addressArray;
    protected String[] cityArray;
    protected String[] stateArray;
    protected String[] zipArray;

    protected String[] propertyID;

    Session s;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties);
        getSupportActionBar().setTitle("Your Properties");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        s = new Session(getApplicationContext());

        try {
            populateArrays();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // if arrays are null, there was a connection issue
        if(priceArray == null) {
            Toast.makeText(getApplicationContext(), "Failed to connect or there are no properties", Toast.LENGTH_LONG).show();
        } else {
            // creating custom list with custom adapter
            ListingListAdapter lla = new ListingListAdapter(this, imageArray, priceArray, numBedArray, addressArray, cityArray, stateArray, zipArray);
            lv = findViewById(R.id.listView1);
            lv.setAdapter(lla);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent in = new Intent(Properties.this, ListingDetails.class);

                    // send over whatever information is needed
                    in.putExtra("PropertyID", propertyID[position]);

                    startActivity(in);
                    finish();
                }
            });
        }

    }

    // going to be used to populate array from database
    public void populateArrays() throws ExecutionException, InterruptedException {
        // Array List that holds result of the AsyncTask
        // .get() is what returns the results
        ArrayList<String> list = new getPropertyInfo().execute(s.getEmail()).get();


        // if list is null, connection issue
        // if first element in list is there are no properties, toast message
        // else, continue operation
        if(list == null) {
            Toast.makeText(getApplicationContext(), "Failed to connect. Check internet connection.", Toast.LENGTH_LONG).show();
        } else if(list.get(0).equals("There are no properties")) {
            Toast.makeText(getApplicationContext(), "There are no properties", Toast.LENGTH_LONG).show();
        } else {
            String s;
            String[] propertyInfo;

            // initialize each array with the length of the list (number of properties in DB)
            priceArray = new String[list.size()];
            numBedArray = new String[list.size()];
            addressArray = new String[list.size()];
            cityArray = new String[list.size()];
            stateArray = new String[list.size()];
            zipArray = new String[list.size()];
            propertyID = new String[list.size()];
            imageArray = new String[list.size()];

            // iterate through properties from DB and assign values accordingly
            for (int i = 0; i < list.size(); i++) {
                s = list.get(i);

                Log.d("BROKE", s);
                Log.d("BROKE", Integer.toString(list.size()));

                // all info is split by a colon
                propertyInfo = s.split("~");

                Log.d("BROKE", "BROKE: " +propertyInfo[0]);

                // assign values that correspond with output from server
                priceArray[i] = propertyInfo[14];
                numBedArray[i] = propertyInfo[10];
                addressArray[i] = propertyInfo[4];
                cityArray[i] = propertyInfo[5];
                stateArray[i] = propertyInfo[6];
                zipArray[i] = propertyInfo[7];
                propertyID[i] = propertyInfo[0];

                imageArray[i] = propertyInfo[22];

            }
        }

    }

    private class getPropertyInfo extends AsyncTask<String, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            try {

                String email = strings[0];

                // link to script on server
                String link = "http://akari.alsolaim.com/Mobile_App_Scripts/getUserPropertiesMobile.php";

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
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
