package com.geotechpy.geostock.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Holds login information. Also network requests
 */
public class GeotechpyStockApp extends Application {

    private static volatile String userName;
    private static RequestQueue requestQueue;
    private static volatile GeotechpyStockApp appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        setAppInstance();
    }

    synchronized void setAppInstance(){
        appInstance = this;
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

    public synchronized static void setUserName(String userCode){
        userName = userCode;
    }


}
