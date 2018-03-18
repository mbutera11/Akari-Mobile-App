package term_project.android.wku.edu.akari;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.listings:
                Intent intent = new Intent(this, Listings.class);
                this.startActivity(intent);
                break;
            case R.id.properties:
                intent = new Intent(this, Properties.class);
                this.startActivity(intent);
                break;
            case R.id.profile:
                intent = new Intent(this, Profile.class);
                this.startActivity(intent);
                break;
            case R.id.login:
                intent = new Intent(this, LoginLogout.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

        public boolean onCreateOptionsMenu(Menu menu) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            return true;
        }

}
