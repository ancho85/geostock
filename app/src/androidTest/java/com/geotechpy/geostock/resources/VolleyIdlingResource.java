package com.geotechpy.geostock.resources;

import android.support.test.espresso.IdlingResource;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.geotechpy.geostock.app.GeotechpyStockApp;

import java.lang.reflect.Field;
import java.util.Set;

import static com.android.support.test.deps.guava.base.Preconditions.checkNotNull;

public final class VolleyIdlingResource implements IdlingResource {
    //private static final String TAG = "VolleyIdlingResource";
    private final String resourceName;

    // written from main thread, read from any thread.
    private volatile ResourceCallback resourceCallback;

    private Field mCurrentRequests;
    private RequestQueue mVolleyRequestQueue;

    public VolleyIdlingResource(String resourceName) throws SecurityException, NoSuchFieldException {
        this.resourceName = checkNotNull(resourceName);
        mVolleyRequestQueue = GeotechpyStockApp.getRequestQueue();
        mCurrentRequests = RequestQueue.class.getDeclaredField("mCurrentRequests");
        mCurrentRequests.setAccessible(true);
    }

    @Override
    public String getName() {
        return resourceName;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isIdleNow() {
        try {
            Set<Request> set = (Set<Request>) mCurrentRequests.get(mVolleyRequestQueue);
            int count = set.size();

            if (count == 0) {
                resourceCallback.onTransitionToIdle();
            }
            return count == 0;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }

}