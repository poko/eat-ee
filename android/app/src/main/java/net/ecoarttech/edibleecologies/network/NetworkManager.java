package net.ecoarttech.edibleecologies.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by pkoronkevich on 4/2/15.
 */
public class NetworkManager {
    private static final String TAG = "NetworkManager";

    private RequestQueue requestQueue;
    //private ImageLoader mImageLoader;

    private static NetworkManager mInstance = null;

    private NetworkManager() {
    }

    public static NetworkManager getInstance() {
        if(mInstance == null) {
            mInstance = new NetworkManager();
        }
        return mInstance;
    }

    public static void initialize(Context context) {
        getInstance().initializeInternal(context);
    }

    private void initializeInternal(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static Request<?> sendRequest(Request<?> request){
        Log.d(TAG, "Sending request to: " + request.getUrl());
        return getInstance().requestQueue.add(request);
    }
}
