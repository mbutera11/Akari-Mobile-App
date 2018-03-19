package term_project.android.wku.edu.akari;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Listings extends AppCompatActivity {

    // later will need to populate these from a database
    Integer[] imageIDarray = {};

    String[] priceArray = {};
    String[] numBedArray = {};
    String[] addressArray = {};
    String[] cityArray = {};
    String[] stateArray = {};
    String[] zipArray = {};

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);
        getSupportActionBar().setTitle("Available Listings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
