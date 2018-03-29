package term_project.android.wku.edu.akari;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    protected Button properties;
    protected Button postProperty;
    protected TextView firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Your Profile");

        properties = findViewById(R.id.properties);
        postProperty = findViewById(R.id.postProperty);

        // testing login with this--------------------
        firstName = findViewById(R.id.firstName);
        firstName.setText(getIntent().getStringExtra("FirstName"));

        properties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Properties.class));
            }
        });
        postProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, postProperty.class));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
