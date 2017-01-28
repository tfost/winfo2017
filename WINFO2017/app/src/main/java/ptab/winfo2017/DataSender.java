package ptab.winfo2017;

/**
 * Created by Tyler on 1/28/2017.
 */

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A Datasender is an abstraction for the file database. It wraps information for a user in a nice,
 * easy to handle way.
 */
public class DataSender {

    private User user; // the user this data sends information about.
    private FirebaseDatabase database;
    /**
     * Creates a new data sender for sending data for a given user.
     * @param user the user data will be sent about.
     */
    public DataSender(User user) {
        this.user = user;
        this.database = FirebaseDatabase.getInstance();
    }

    /**
     * Writes the current user information to the database!
     */
    public void writeUser() {
        DatabaseReference userRef = database.getReference("Users/" +user.getUserID());
    }

}
