package term_project.android.wku.edu.akari;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected Session s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!s.getEmail().equals("")) {
                    startActivity(new Intent(getApplicationContext(), PostProperty.class));
                } else {
                    Snackbar.make(view, "Please log in to add property", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // create session object to check login status
        s = new Session(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        // if user is logged in, show one menu
        // else show other menu
        if(!s.getEmail().equals("")) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_home_loggedin);

            // change header info with user info
            View headerView = navigationView.getHeaderView(0);
            TextView email = headerView.findViewById(R.id.email);
            TextView name = headerView.findViewById(R.id.name);

            email.setText(s.getEmail());
            name.setText(s.getFirstName() + " " + s.getLastName());

        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_home_loggedout);

            // change header info to default info
            View headerView = navigationView.getHeaderView(0);
            TextView email = headerView.findViewById(R.id.email);
            TextView name = headerView.findViewById(R.id.name);

            email.setText("");
            name.setText("Please log in to see profile");
        }

        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        if(!s.getEmail().equals("")) {
            switch (item.getItemId()) {
                case R.id.allProperties:
                    Intent intent = new Intent(this, Listings.class);
                    this.startActivity(intent);
                    break;
                // hardcoded id because for some reason it wouldnt work for properties
                case 2131231022:
                    Log.d("CHECK", "HERE");
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
                            Intent in = new Intent(getApplicationContext(), HomePage.class);
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
                case R.id.allProperties:
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
