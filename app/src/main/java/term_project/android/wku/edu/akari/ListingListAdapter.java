// Michael Butera
// Tom Spencer

// used to create a custom list item in the list view when viewing properties (Listings.java)
package term_project.android.wku.edu.akari;

import android.app.Activity;
import android.util.Base64;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by michaelbutera on 3/18/18.
 */

public class ListingListAdapter extends ArrayAdapter{

    //to reference the Activity
    private final Activity context;

    //to store the property images
    private final String[] imageArray;

    private final String[] priceArray;
    private final String[] numBedArray;
    private final String[] addressArray;
    private final String[] cityArray;
    private final String[] stateArray;
    private final String[] zipArray;

    // all information that will be in a customer list item for displaying properties
    public ListingListAdapter(Activity context, String[] imageArray, String[] priceArray, String[] numBedArray, String[] addressArray, String[] cityArray, String[] stateArray, String[] zipArray) {
        super(context,R.layout.listing_row , priceArray);
        this.context = context;
        this.imageArray = imageArray;
        this.priceArray = priceArray;
        this.numBedArray = numBedArray;
        this.addressArray = addressArray;
        this.cityArray = cityArray;
        this.stateArray = stateArray;
        this.zipArray = zipArray;
    }

    // for each list item, set all content, textviews, and imageviews
    public View getView(int position, View view, ViewGroup parent) {

        // get row
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listing_row, null,true);

        // initialize textviews and imageview
        TextView priceText = rowView.findViewById(R.id.price);
        TextView streetText = rowView.findViewById(R.id.street);
        TextView numBedText = rowView.findViewById(R.id.numBedrooms);
        TextView locationText = rowView.findViewById(R.id.location);
        ImageView imageView = rowView.findViewById(R.id.imageView1);

        // set text views and imageviews
        priceText.setText("$" + priceArray[position]);
        numBedText.setText(numBedArray[position] + " Bedrooms");
        streetText.setText(addressArray[position]);
        locationText.setText(cityArray[position] + ", " + stateArray[position] + " " + zipArray[position]);

        // if image array at row is |, there is no image, set default
        // else, convert base64 to byte[], then to bitmap and then set imageview to bitmap
        if(imageArray[position].equals("|")) {
            imageView.setImageResource(R.drawable.noimage);
        } else {
            byte[] decodedPic = Base64.decode(imageArray[position], Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(decodedPic, 0, decodedPic.length);
            imageView.setImageBitmap(bmp);
        }


        return rowView;

    }

}
