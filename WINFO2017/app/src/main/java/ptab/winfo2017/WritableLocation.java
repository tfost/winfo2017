package ptab.winfo2017;
import com.google.android.gms.maps.model.LatLng;
/**
 * Created by Tyler on 1/28/2017.
 */

/**
 * A WritableLocation is an immutable location that stores a subset of information in the GoogleMaps
 * Location Class, such that it can be easily stored and reformed from a database.
 */
public class WritableLocation {

    private LatLng location;
    private long timeRecorded;

    /**
     * Creates a new WritableLocation from lattitude and longitude coordinates.
     * @param location the location to be stored
     * @param timeRecorded the time the user was at this location.
     */
    public WritableLocation(LatLng location, long timeRecorded) {
        this.location = location;
        this.timeRecorded = timeRecorded;
    }

    public LatLng getLocation() {
        return this.location;
    }

    public long getTimeRecorded() {
        return this.timeRecorded;
    }



}
