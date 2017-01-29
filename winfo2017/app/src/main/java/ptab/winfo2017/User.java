package ptab.winfo2017;

/**
 * Created by Tyler on 1/28/2017.
 */

import java.util.ArrayList;
import java.util.List;

/**
 * A User is someone who uses this app. Each user contains information such as User ID,
 * Contact information, list of previously visited locations, etc.
 */
public class User {
    public String name;
    public List<WritableLocation> locations;
    public List<Contact> contacts;
    private int id;

    public User(String name) {
        this.locations = new ArrayList<>();
        this.contacts = new ArrayList<>();
        this.name = name;
        this.id = -1;
    }

    /**
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the user's id, for use in uniquely identifying the user.
     * @return an integer representation of this user.
     */
    public int getUserID() {
        return this.id;
    }

    public void setUserId(int id) {
        this.id = id;
    }

    public List<Contact> getEmergencyContacts() {
        return this.contacts;
    }

    public List<WritableLocation> getLocations() {
        return this.locations;
    }
}
