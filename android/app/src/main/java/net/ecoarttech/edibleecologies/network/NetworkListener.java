package net.ecoarttech.edibleecologies.network;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

/**
 * Created by pkoronkevich on 4/2/15.
 */
public interface NetworkListener<T> extends Listener<T>, ErrorListener {
}
