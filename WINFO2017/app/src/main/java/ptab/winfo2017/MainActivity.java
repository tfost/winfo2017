package ptab.winfo2017;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

import android.location.LocationListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.io.File;
import java.util.Calendar;

import static ptab.winfo2017.R.id.textView;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String SETTINGS_FILE_PATH = "settings.txt";
    private User user;
    private WritableLocation location;
    private GoogleMap mMap;
    private FirebaseDatabase database;
    private DataSender sender;
    private Calendar time = Calendar.getInstance();
    private GoogleApiClient mGoogleApiClient;
    private String tag = "MainActivity";
    private LocationManager locationManager;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    private boolean isBeingTouched = false;

    public void onConnectionFailed(ConnectionResult result) {
        Log.d(tag, "Failed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        database = FirebaseDatabase.getInstance();
        user = new User("testuser");
        user.getLocations().add(new WritableLocation(new LatLng(100, 100), 100000));
        sender = new DataSender(user, database);
        sender.setUserNextId();


        File file = new File(this.getFilesDir(), SETTINGS_FILE_PATH);
        if(!file.exists()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
        Log.d(tag, "Network provider");
//        if (locationManager != null) {
//            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
//                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                locationManager.removeUpdates(GPSListener.this);
//            }
//        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button button = (Button) findViewById(R.id.start_track);
        button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Log.d("MainActivity","touched");
                    isBeingTouched = true;
                }
                if(event.getAction() == MotionEvent.ACTION_UP){
                    Log.d("MainActivity","untouched");
                    isBeingTouched = false;
                }

                return true;
            }

        });

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void onConnectionSuspended(int num) {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Attempt to request permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, mLocationListener);
        Location locationMarker = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.d(tag, "GPS provider");


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST);
            }
        }

    }

    private final LocationListener mLocationListener = new LocationListener() {

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
        public LatLng prev = null;
        public MarkerOptions last = null;
        @Override
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Log.d(tag, "Latitude: " + latitude + ", Longitude: " + longitude);
            LatLng locationMarker = new LatLng(latitude, longitude);
            if (prev != null) {
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(prev, locationMarker)
                        .width(5)
                        .color(Color.RED));
                if (last != null) {
                    last.visible(false);
                }
            }
            prev = locationMarker;
            Log.d(tag, "Time: " + System.currentTimeMillis());
            if (isBeingTouched) {
                if (mMap != null) {
                    last = new MarkerOptions().position(locationMarker);
                    mMap.addMarker(last);
                    user.getLocations().add(new WritableLocation(locationMarker, System.currentTimeMillis()));
                    sender.writeUser();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(locationMarker));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationMarker, 18));
                }
            }


        }
    };


}