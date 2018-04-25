package term_project.android.wku.edu.akari;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class AddPictureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture);

        String userID = getIntent().getStringExtra("userID");
        String propID = getIntent().getStringExtra("propertyID");


    }
}
