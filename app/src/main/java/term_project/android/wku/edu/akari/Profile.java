package term_project.android.wku.edu.akari;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    protected Session s;

    protected Button properties;
    protected Button postProperty;
    protected Button startTenency;
    protected TextView firstName;
    protected TextView lastName;
    protected TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Your Profile");

        s = new Session(getApplicationContext());

        firstName = findViewById(R.id.firstName);
        firstName.setText(s.getFirstName());
        lastName = findViewById(R.id.lastName);
        lastName.setText(s.getLastName());
        email = findViewById(R.id.email);
        email.setText(s.getEmail());


        properties = findViewById(R.id.properties);
        postProperty = findViewById(R.id.postProperty);
        startTenency = findViewById(R.id.tenency);

        properties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, Properties.class));
            }
        });
        postProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, PostProperty.class));
            }
        });
        startTenency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, StartTenency.class));
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
