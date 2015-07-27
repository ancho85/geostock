package com.geotechpy.geostock.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.geotechpy.geostock.MainActivity;
import com.geotechpy.geostock.R;
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

    public synchronized static void addToRequestQueue(Request request){
        NetRequester.getInstance(appInstance).addToRequestQueue(request);
    }

    public String getUserName(){
        return userName;
    }

    public synchronized static void setUserName(String userCode){
        userName = userCode;
    }

    public void respawnLogin(Context mContext){
        /*
         * The user leaves the app (with home button) and after a while the OS's kills the app
         * to save memory. Thus the current activity is recreated without userName information.
         * http://www.developerphil.com/dont-store-data-in-the-application-object/
         */
        Toast.makeText(mContext, R.string.session_expired, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class); //Relogin
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ((Activity)mContext).finish(); // call this to finish the current activity
    }


}
