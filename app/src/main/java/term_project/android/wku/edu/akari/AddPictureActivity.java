package term_project.android.wku.edu.akari;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class AddPictureActivity extends AppCompatActivity {

    protected String userID;
    protected String propID;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    protected Button takePic;
    protected Button upload;
    protected ImageView image;

    protected Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture);

        takePic = findViewById(R.id.takePic);
        upload = findViewById(R.id.upload);
        image = findViewById(R.id.image1);

        userID = getIntent().getStringExtra("userID");
        propID = getIntent().getStringExtra("propertyID");


        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

    }

    public void upload(View view) {
        // if bitmap is null, picture was not taken
        // else, convert image to byte array and then get the string base64 version of the byte array to send to the server
        if(bitmap == null) {
            Toast.makeText(getApplicationContext(), "Please take a picture to upload", Toast.LENGTH_LONG).show();
        } else {
            // convert bitmap to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            String imageString = Base64.encodeToString(byteArray, 0);

            // send image string, the user id, and property id to AsyncTask
            new UploadImage().execute(imageString, userID, propID);
        }
    }


    // handle image once picture is taken
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle extras = data.getExtras();
        bitmap = (Bitmap) extras.get("data");
        image.setImageBitmap(bitmap);
    }

    private class UploadImage extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {

                // parameters sent to AsyncTask
                String image = strings[0];
                String user = strings[1];
                String prop = strings[2];

                // link to script on server
                String link = "http://akari.alsolaim.com/Mobile_App_Scripts/uploadImageMobile.php";

                // encode data to URL to pass via POST method
                String data = URLEncoder.encode("imgString", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8");
                data += "&" + URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8");
                data += "&" + URLEncoder.encode("propID", "UTF-8") + "=" + URLEncoder.encode(prop, "UTF-8");

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
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                // return server output
                return sb.toString();

            } catch (Exception e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            // if server returns uploaded, picture was stored on server successfully
            // else, something went wrong.
            if(s.equals("UPLOADED")) {
                Toast.makeText(getApplicationContext(), "Your picture was successfully uploaded and property was posted", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(getApplicationContext(), Listings.class));
            } else {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }
    }




}
