package term_project.android.wku.edu.akari;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Listings extends AppCompatActivity {

    // later will need to populate these from a database
    Integer[] imageIDarray = {};

    // Used for testing

//    String[] priceArray = {"1000", "1250"};
//    String[] numBedArray = {"3", "5"};
//    String[] addressArray = {"154 Waters Edge Lane", "1909 Creason Street"};
//    String[] cityArray = {"Hendersonville", "Bowling Green"};
//    String[] stateArray = {"TN", "KY"};
//    String[] zipArray = {"37075", "42101"};

    String[] priceArray;
    String[] numBedArray;
    String[] addressArray;
    String[] cityArray;
    String[] stateArray;
    String[] zipArray;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);
        getSupportActionBar().setTitle("Available Listings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        populateArrays();

        // creating custom list with custom adapter
        ListingListAdapter lla = new ListingListAdapter(this, imageIDarray, priceArray, numBedArray, addressArray, cityArray, stateArray, zipArray);
        lv = findViewById(R.id.listView1);
        lv.setAdapter(lla);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(Listings.this, ListingDetails.class);

                // send over whatever information is needed
                // in.putExtra("", );

                startActivity(in);
                finish();
            }
        });

    }

    // going to be used to populate array from database
    public void populateArrays() {
        new getPropertyInfo().execute();
    }

    private class getPropertyInfo extends AsyncTask<Void, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            try {
                // link to script on server
                String link = "http://akari.alsolaim.com/Mobile_App_Scripts/getPropertiesMobile.php";

                // link to local script
                //String link = "http://10.0.1.42/Code/Mobile_App_Scripts/getPropertiesMobile.php";


                // create a URL with the link
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                // will read information received from the server
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                ArrayList<String> list = new ArrayList<String>();

                String line;

                // Read Server Response and append to string builder
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
        protected void onPostExecute(ArrayList<String> list) {

            String s;
            String[] propertyInfo;

            priceArray = new String[list.size()];
            numBedArray = new String[list.size()];
            addressArray = new String[list.size()];
            cityArray = new String[list.size()];
            stateArray = new String[list.size()];
            zipArray = new String[list.size()];

            int count = 0;

            for(int i = 0; i < list.size(); i++) {
                s = list.get(i);
                propertyInfo = s.split(":");
                priceArray[i] = propertyInfo[14];
                Log.d("PRICE", "Price: " + propertyInfo[14]);
                numBedArray[i] = propertyInfo[10];
                addressArray[i] = propertyInfo[4];
                cityArray[i] = propertyInfo[5];
                stateArray[i] = propertyInfo[6];
                zipArray[i] = propertyInfo[7];
                count++;
            }

            for(int i = 0; i < addressArray.length; i++) {
                Log.d("ADDRESS", "Address: " + addressArray[i]);
            }

            Log.d("COUNT", "Count: " + count);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
