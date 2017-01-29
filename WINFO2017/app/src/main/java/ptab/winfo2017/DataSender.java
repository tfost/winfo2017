package ptab.winfo2017;

/**
 * Created by Tyler on 1/28/2017.
 */

import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A Datasender is an abstraction for the file database. It wraps information for a user in a nice,
 * easy to handle way.
 */
public class DataSender {

    public static final String USERDIR = "Users";
    public static final String NEXTID = "nextUserID";
    private User user; // the user this data sends information about.
    private FirebaseDatabase database;
    /**
     * Creates a new data sender for sending data for a given user.
     * @param user the user data will be sent about.
     */
    public DataSender(User user, FirebaseDatabase database) {
        this.user = user;
        this.database = database;
    }

    /**
     * Writes the current user information to the database!
     */
    public void writeUser() {
        DatabaseReference userRef = database.getReference();
        userRef.child(USERDIR).child("" + user.getUserID()).child("Locations").setValue(user.getLocations());
        userRef.child(USERDIR).child("" + user.getUserID()).child("Name").setValue(user.getName());
    }


    //maybe gets a new uesr id?
    public void setUserNextId() {
        DatabaseReference userRef = database.getReference();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int id =  Integer.parseInt(dataSnapshot.child(NEXTID).getValue().toString());
                /*if (res == null || res.equals("null")) {

                    Log.d("DATASENDER", "SOMETHING WENT WRONG WITH ID LOOKUP");
                }*/
               // int id = Integer.parseInt(res);
                user.setUserId(id);
                DatabaseReference newRef = database.getReference();
                newRef.child(NEXTID).setValue(id + 1);
                Log.d("DataSEnder", "userid = " + id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DATASENDER", "Something happened and cancelled the thing");
            }
        });
    }




}
