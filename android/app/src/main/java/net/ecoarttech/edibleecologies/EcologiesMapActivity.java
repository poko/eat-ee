package net.ecoarttech.edibleecologies;

import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.ecoarttech.edibleecologies.model.Waypoint;
import net.ecoarttech.edibleecologies.network.APIClient;
import net.ecoarttech.edibleecologies.network.NetworkListener;
import net.ecoarttech.edibleecologies.network.NetworkManager;
import net.ecoarttech.edibleecologies.util.InterviewPanelHelper;
import net.ecoarttech.edibleecologies.util.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EcologiesMapActivity extends FragmentActivity implements GoogleMap.OnMapLoadedCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private static final String TAG = "EdibleEcologiesTag";

    private GoogleApiClient mGoogleApiClient;
    // views
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private FloatingActionButton mPinButton;

    //data
    private double mUserLat = 30; //TODO - set defaults if user doesn't have location enabled or something
    private double mUserLng = -97;
    private Location mLastLocation;
    private ArrayList<Waypoint> mDownloadedWaypoints;

    // misc
    private InterviewPanelHelper mPanelHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecologies_map);
        // initialize networking
        NetworkManager.initialize(getApplicationContext());
        // TODO - check if google play services isn't available
        buildGoogleApiClient();
        // TODO - check if location isn't enabled.

        // download current interview questions
        APIClient.getQuestions(new InterviewQuestionsListener());

        // download nearby waypoints
        mDownloadedWaypoints = new ArrayList<>();
        WaypointDownloadListener waypointListener = new WaypointDownloadListener();
        APIClient.getWaypoints(mUserLat, mUserLng, waypointListener);
        //add these to map in onMapLoaded instead.

        // get user's location

        // setup action bar

        // setup map
        setUpMapIfNeeded();

        // set up other ui elements
        mPinButton = (FloatingActionButton) findViewById(R.id.btn_pin);
        mPinButton.setOnClickListener(this);
        mPanelHelper = new InterviewPanelHelper(getLayoutInflater(), (LinearLayout) findViewById(R.id.interview));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_pin:{
                // toggle interview panel ?
                mPanelHelper.toggleInterviewPanel();
                break;
            }
        }
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setOnMapLoadedCallback(this);
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LatLng position = new LatLng(mUserLat, mUserLng);
        mMap.addMarker(new MarkerOptions().position(position));
        // zoom user to map
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
    }

    @Override
    public void onMapLoaded() {
        setUpMap();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mUserLat = mLastLocation.getLatitude();
            mUserLng = mLastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //TODO - display error
    }

    private void loadLocalQuestions(){
        mPanelHelper.setQuestions(SharedPrefs.getQuestions());
    }

    private class InterviewQuestionsListener implements NetworkListener<JSONArray> {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            //default to questions currently saved locally
            loadLocalQuestions();
        }

        @Override
        public void onResponse(JSONArray jsonArray) {
            try {
                ArrayList<String> questions = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject question = (JSONObject) jsonArray.get(i);
                    questions.add(question.getString("text"));
                }
                // save to shared prefs
                SharedPrefs.saveQuestions(questions);
                loadLocalQuestions();
            }
            catch (JSONException ex){
                loadLocalQuestions();
            }
        }

    }

    private class WaypointDownloadListener implements NetworkListener<JSONArray> {

        private Geocoder geocoder = new Geocoder(getApplicationContext());

        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }

        @Override
        public void onResponse(JSONArray jsonArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    Waypoint point = new Waypoint(jsonArray.getJSONObject(i), geocoder);
                    mDownloadedWaypoints.add(i, point);
                    mMap.addMarker(new MarkerOptions().position(point.getLocation())
                                                        .title(point.getCity())
                                                        .snippet(point.getDetails()));
                } catch (JSONException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        }

    }
}
