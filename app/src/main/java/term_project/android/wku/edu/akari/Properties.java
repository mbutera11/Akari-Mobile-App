package term_project.android.wku.edu.akari;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Properties extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties);
        getSupportActionBar().setTitle("Your Properties");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
