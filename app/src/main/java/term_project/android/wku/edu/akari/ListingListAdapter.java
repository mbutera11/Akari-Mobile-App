// used to create a custom list item in the list view when viewing properties (Listings.java)

package term_project.android.wku.edu.akari;

import android.app.Activity;
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
    private final Integer[] imageIDarray;

    private final String[] priceArray;
    private final String[] numBedArray;
    private final String[] addressArray;
    private final String[] cityArray;
    private final String[] stateArray;
    private final String[] zipArray;

    public ListingListAdapter(Activity context, Integer[] imageIDarray, String[] priceArray, String[] numBedArray, String[] addressArray, String[] cityArray, String[] stateArray, String[] zipArray) {
        super(context,R.layout.listing_row , priceArray);
        this.context = context;
        this.imageIDarray = imageIDarray;
        this.priceArray = priceArray;
        this.numBedArray = numBedArray;
        this.addressArray = addressArray;
        this.cityArray = cityArray;
        this.stateArray = stateArray;
        this.zipArray = zipArray;
    }

    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater=context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listing_row, null,true);

        TextView priceText = rowView.findViewById(R.id.price);
        TextView streetText = rowView.findViewById(R.id.street);
        TextView numBedText = rowView.findViewById(R.id.numBedrooms);
        TextView locationText = rowView.findViewById(R.id.location);
        ImageView imageView = rowView.findViewById(R.id.imageView1);

        priceText.setText("$" + priceArray[position]);
        numBedText.setText(numBedArray[position] + " Bedrooms");
        streetText.setText(addressArray[position]);
        locationText.setText(cityArray[position] + ", " + stateArray[position] + " " + zipArray[position]);
        imageView.setImageResource(imageIDarray[position]);

        return rowView;

    };

}
