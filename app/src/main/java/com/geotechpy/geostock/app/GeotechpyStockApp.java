package com.geotechpy.geostock.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Holds login information. Also network requests
 */
public class GeotechpyStockApp extends Application {

    private static String userName = "";
    private static RequestQueue requestQueue;
    private static GeotechpyStockApp appInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        requestQueue = Volley.newRequestQueue(this);
    }

    public synchronized static RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(appInstance);
        }
        return requestQueue;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userCode){
        userName = userCode;
    }


}
