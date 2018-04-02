package term_project.android.wku.edu.akari;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    protected Session s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create session object to check login status
        s = new Session(this);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // if user is logged in, these options are shown
        // else show others
        if(!s.getEmail().equals("")) {
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
                case R.id.logout:
                    // create alert:
                    // asks if user really wants to log out or not
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Log Out").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // if user wants to log out, destroy session and take back to main activity
                            s.destroy();
                            Intent in = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(in);
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // user clicks no, nothing happens
                        }
                    });

                    // show the alert
                    AlertDialog d = builder.create();
                    d.setTitle("Are you sure you want to log out?");
                    d.show();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } else {
            switch (item.getItemId()) {
                case R.id.listings:
                    Intent intent = new Intent(this, Listings.class);
                    this.startActivity(intent);
                    break;
                case R.id.login:
                    intent = new Intent(this, Login.class);
                    this.startActivity(intent);
                    break;
                case R.id.register:
                    intent = new Intent(this, Register.class);
                    this.startActivity(intent);
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        return true;
    }

    // if user is logged in, show loggedin_menu
    // else show loggedout_menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if(!s.getEmail().equals("")) {
            inflater.inflate(R.menu.loggedin_menu, menu);
        } else {
            inflater.inflate(R.menu.loggedout_menu, menu);
        }

        return true;
    }

}
