package net.ecoarttech.edibleecologies.model;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by mollyrand on 4/16/15.
 */
public class Waypoint {

    private int mId;
    private double mLat;
    private double mLong;
    private String mTimeStamp;
    private Interview mInterview;
    private String mLocality;

    public Waypoint(JSONObject object, Geocoder geocoder) {
        try {
            this.mId = object.getInt("_id");
            this.mLong = object.getDouble("lng");
            this.mLat = object.getDouble("lat");
            this.mTimeStamp = object.getString("timestamp");
            List<Address> list = geocoder.getFromLocation(mLat, mLong, 1);
            if (list.size() > 0) {
                Address address = list.get(0);
                this.mLocality = address.getLocality();
            }

        } catch (Exception e) {
            Log.e("WaypointError", e.getLocalizedMessage());
        }
    }

    public LatLng getLocation() {
        return new LatLng(this.mLat, this.mLong);
    }

    public String getDetails() {
        return "Tap for details!";
    }

    public String getCity() {
        return this.mLocality;
    }
}
