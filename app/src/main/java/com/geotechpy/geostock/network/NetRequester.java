package com.geotechpy.geostock.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Singleton for volley requests
 */
public class NetRequester {
    private static NetRequester mInstance = null;
    private RequestQueue requestQueue;
    private static Context mContext;

    private NetRequester(Context context) {
        mContext = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized NetRequester getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetRequester(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
