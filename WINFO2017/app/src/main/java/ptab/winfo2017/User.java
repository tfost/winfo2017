package ptab.winfo2017;

/**
 * Created by Tyler on 1/28/2017.
 */

import java.util.List;

/**
 * A User is someone who uses this app. Each user contains information such as User ID,
 * Contact information, list of previously visited locations, etc.
 */
public class User {


    public User() {}

    /**
     *
     * @return the user's name
     */
    public String getName() {
        return "";
    }

    /**
     * Returns the user's id, for use in uniquely identifying the user.
     * @return an integer representation of this user.
     */
    public int getUserID() {
        return -1;
    }

    public List<EmergencyContact> getEmergencyContacts() {
        return null;
    }

    public List<WritableLocation> getLocations() {
        return null;
    }
}
