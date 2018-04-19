package term_project.android.wku.edu.akari;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import term_project.android.wku.edu.akari.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        address = getIntent().getStringExtra("address");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at address location
        LatLng location = null;
        try {
            // location is the LatLng object returned from the getCoordinates method
            location = getCoordinates(getApplicationContext(), address);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // add marker with the location lat, long coordinates
        // also sets title of marker to the name passed from the main activity and shows it
        Marker marker = mMap.addMarker(new MarkerOptions().position(location).title(address));
        marker.showInfoWindow();

        // moves camera to the location and sets zoom to 15
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }


    // converts street address to LatLng object
    // "Reverse Geocoding"
    public LatLng getCoordinates(Context c, String streetAddress) throws IOException {
        // create geocoder object
        Geocoder g = new Geocoder(c);

        // list of Address objects
        // will only hold 1 address, streetAddress
        List<Address> address = g.getFromLocationName(streetAddress, 1);

        // if the address list is null, it couldnt get location from street address
        // return null
        if(address == null) {
            return null;
        }

        // Address object of the first Address in the address list
        Address location = address.get(0);

        // create LatLng object with the locations coordinates to return
        LatLng place = new LatLng(location.getLatitude(), location.getLongitude());

        // return latitude and longitude of the streetAddress parameter
        return place;


    }
}
