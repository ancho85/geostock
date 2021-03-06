package com.geotechpy.geostock.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
    private static volatile String stockType;
    private static volatile String lastToastMessage; //for espresso tests
    private static volatile boolean fakeHttpConnections = false; //for espresso tests
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

    @SuppressWarnings("unchecked")
    public synchronized static void addToRequestQueue(Request request){
        NetRequester.getInstance(appInstance).addToRequestQueue(request);
    }

    public synchronized static String getUserName(){
        return userName;
    }

    public synchronized static void setStockType(String type){
        stockType = type;
    }

    public synchronized static String getStockType(){
        return stockType;
    }

    public synchronized static void setUserName(String userCode){
        userName = userCode;
    }

    public synchronized static void setLastToastMessage(String msg){
        lastToastMessage = msg;
    }

    public synchronized static String getLastToastMessage(){
        return lastToastMessage;
    }

    public synchronized static void setFakeHttpConnections(boolean isFake){
        fakeHttpConnections = isFake;
    }

    public synchronized static boolean getFakeHttpConnections(){
        return fakeHttpConnections;
    }

    public void respawnLogin(Context mContext){
        /*
         * The user leaves the app (with home button) and after a while the OS's kills the app
         * to save memory. Thus the current activity is recreated without userName information.
         * http://www.developerphil.com/dont-store-data-in-the-application-object/
         */
        displayMessage(mContext.getString(R.string.session_expired));
        Intent intent = new Intent(this, MainActivity.class); //Relogin
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        ((Activity)mContext).finish(); // call this to finish the current activity
    }

    public synchronized static void displayMessage(String msg){
        Toast.makeText(appInstance, msg, Toast.LENGTH_SHORT).show();
        setLastToastMessage(msg);
    }

    public synchronized static void showEditTextSoftKeyboard(Context mContext, EditText editText){
        if (!getFakeHttpConnections()){
            InputMethodManager imm =  (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }


}
