package com.geotechpy.geostock.app;

import android.app.Application;

/**
 * Holds login information
 */
public class GeotechpyStockApp extends Application {

    private static String userName = "";

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userCode){
        userName = userCode;
    }

}
