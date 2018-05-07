// Michael Butera
// Tom Spencer

package term_project.android.wku.edu.akari;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ListingDetails extends AppCompatActivity {

    protected TextView ad1, ad2, pr1, pr2, de, pro, ty, av, con, csz, coun, si, bed, bath;
    protected ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);
        getSupportActionBar().setTitle("Listing Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        overridePendingTransition(R.anim.in, R.anim.out);

        // get property ID from intent in the listings page
        String propertyID = getIntent().getStringExtra("PropertyID");

        // initialize all text views
        ad1 = findViewById(R.id.street);
        ad2 = findViewById(R.id.streetDetails);
        pr1 = findViewById(R.id.price);
        pr2 = findViewById(R.id.priceInfo);
        de = findViewById(R.id.description);
        pro = findViewById(R.id.problems);
        ty = findViewById(R.id.propertyType);
        av = findViewById(R.id.available);
        con = findViewById(R.id.construction);
        csz = findViewById(R.id.cityStateZip);
        coun = findViewById(R.id.country);
        si = findViewById(R.id.size);
        bed = findViewById(R.id.numBeds);
        bath = findViewById(R.id.numBath);

        // initialize imageview
        imageView = findViewById(R.id.detailImage);

        // execute AsyncTask
        new ListingDetailAction().execute(propertyID);

        // start map activity when button is clicked, send over address
        // click on either address on the page
        ad1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ListingDetails.this, MapsActivity.class);
                String addressText = ad2.getText() + " " + csz.getText();
                in.putExtra("address", addressText);
                startActivity(in);

            }
        });
        // start map activity when button is clicked, send over address
        ad2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ListingDetails.this, MapsActivity.class);
                String addressText = ad2.getText() + " " + csz.getText();
                in.putExtra("address", addressText);
                startActivity(in);

            }
        });

    }

    // sets all text views on the page with all the information from the server
    // called during postExecute of AsyncTask
    protected void setTextViews(String address, String price, String description, String problems,
                                String type, String available, String construction, String cityStateZip,
                                String country, String size, String bedrooms, String bathrooms, String imageString) {

        // set text views
        ad1.setText(address);
        ad2.setText(address);
        pr1.setText(price);
        pr2.append(" " + price);
        de.setText(description);
        pro.append(" " + problems);
        ty.append(" " + type);
        av.append(" " + available);
        con.append(" " + construction);
        csz.setText(cityStateZip);
        coun.setText(country);
        si.append(": " + size);
        bed.append(": " + bedrooms);
        bath.append(": " + bathrooms);

        // set image from server
        // if the string is |, that means no image for that property, set to default noImage
        // else, convert base64 string to byte array, then to bitmap, then set image view to bitmap
        if(imageString.equals("|")) {
            imageView.setImageResource(R.drawable.noimage);
        } else {
            byte[] decodedPic = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(decodedPic, 0, decodedPic.length);
            imageView.setImageBitmap(bmp);
        }


    }

    // handles server interaction
    private class ListingDetailAction extends AsyncTask<String, Integer, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            try {
                // to be returned
                ArrayList<String> list = new ArrayList<>();
                String propID = strings[0];

                // link to script on server
                String link = "http://akari.alsolaim.com/Mobile_App_Scripts/getListingDetailsMobile.php";

                // encode the propertyID to pass to the script via POST
                String data  = URLEncoder.encode("propertyID", "UTF-8") + "=" + URLEncoder.encode(propID, "UTF-8");

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
            super.onPostExecute(strings);
            
            // get string from array list and split it by ~
            String info = strings.get(0);

            // if list is null, there is a connection issue
            // else, continue with the operations
            if(info == null) {
                Toast.makeText(getApplicationContext(), "Failed to connect. Check internet connection", Toast.LENGTH_LONG).show();
            } else {
                String[] propertyInfo = info.split("~");

                // set variable to corresponding index from server response
                String address = propertyInfo[4];
                String price = "$" + propertyInfo[14];
                String description = propertyInfo[12];
                String problems = propertyInfo[17];
                String type = propertyInfo[8];
                String available = propertyInfo[15];
                String construction = propertyInfo[16];
                String cityStateZip = propertyInfo[5] + ", " + propertyInfo[6] + " " + propertyInfo[7];
                String country = propertyInfo[3];
                String size = propertyInfo[9];
                String bedrooms = propertyInfo[10];
                String bathrooms = propertyInfo[11];
                String imageString = propertyInfo[22];

                // call this method to set all text views in the page with info gathered from string array
                setTextViews(address, price, description, problems, type, available, construction, cityStateZip, country, size, bedrooms, bathrooms, imageString);
            }

        }

    }



    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
