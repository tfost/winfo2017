package ptab.winfo2017;

/**
 * Created by Tyler on 1/28/2017.
 */

/**
 * A WritableLocation is an immutable location that stores a subset of information in the GoogleMaps
 * Location Class, such that it can be easily stored and reformed from a database.
 */
public class WritableLocation {

    private double lat;
    private double lng;
    private long timeRecorded;

    /**
     * Creates a new WritableLocation from lattitude and longitude coordinates.
     * @param lat the lattitude of the location
     * @param lng the longitude of the location
     */
    public WritableLocation(double lat, double lng, long timeRecorded) {
        this.lat = lat;
        this.lng = lng;
        this.timeRecorded = timeRecorded;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLng() {
        return this.lng;
    }

    public long getTimeRecorded() {
        return this.timeRecorded;
    }



}
