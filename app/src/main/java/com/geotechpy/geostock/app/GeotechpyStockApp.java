package com.geotechpy.geostock.app;

import android.app.Application;

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

    public String getUserName(){
        return userName;
    }

    public synchronized static void setUserName(String userCode){
        userName = userCode;
    }


}
