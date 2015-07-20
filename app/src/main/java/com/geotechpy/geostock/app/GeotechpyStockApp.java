package com.geotechpy.geostock.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.geotechpy.geostock.network.NetRequester;

/**
 * Holds login information.
 */
public class GeotechpyStockApp extends Application {

    private static volatile String userName;
    private static volatile GeotechpyStockApp appInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        setAppInstance(this);
    }

    synchronized static void setAppInstance(GeotechpyStockApp application){
        appInstance = application;
    }

    public synchronized static RequestQueue getRequestQueue(){
        return NetRequester.getInstance(appInstance).getRequestQueue();
    }

    public String getUserName(){
        return userName;
    }

    public synchronized static void setUserName(String userCode){
        userName = userCode;
    }


}
