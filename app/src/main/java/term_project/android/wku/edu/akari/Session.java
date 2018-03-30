package term_project.android.wku.edu.akari;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by michaelbutera on 3/29/18.
 * Used to keep track of the logged in user
 *
 * setEmail: stores logged in user's email in session for reference
 * getEmail: returns logged in user's email
 *
 * setFirstName: stores logged in user's first name in session for reference
 * getFirstName: returns logged in user's first name
 *
 * setlastName: stores logged in user's last name in session for reference
 * getLastName: returns logged in user's last name
 *
 * destroy: clears shared preference of all stored user information, used in logout functionality
 *
 */

public class Session {

    private SharedPreferences pref;

    public Session(Context c) {
        pref = PreferenceManager.getDefaultSharedPreferences(c);
    }

    public void setEmail(String email) {
        pref.edit().putString("email", email).commit();
    }

    public String getEmail() {
        String email = pref.getString("email", "");
        return email;
    }

    public void setFirstName(String firstName) {
        pref.edit().putString("FirstName", firstName).commit();
    }

    public String getFirstName() {
        String firstName = pref.getString("FirstName", "");
        return firstName;
    }

    public void setLastName(String lastName) {
        pref.edit().putString("LastName", lastName).commit();
    }

    public String getLastName() {
        String lastName = pref.getString("LastName", "");
        return lastName;
    }

    public void destroy() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

}
