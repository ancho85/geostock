package com.geotechpy.geostock.network.dev;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.NoCache;

import java.io.UnsupportedEncodingException;

public class FakeRequestQueue extends RequestQueue {
    public FakeRequestQueue(Context context) {
        super(new NoCache(), new BasicNetwork(new FakeHttpStack(context)));
        start();
    }

    @Override
    public void start() {
        Log.d("FAKE", "request start");
        super.start();
    }

    @Override
    public void stop() {
        Log.d("FAKE", "request stop");
        super.stop();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Request add(Request request) {
        Log.w("FAKE", "Note: FakeRequestQueue is used");
        Log.d("FAKE",
                "New request " + request.getUrl() + " is added with priority " + request.getPriority());
        try {
            if (request.getBody() == null) {
                Log.d("FAKE", "body is null");
            } else {
                Log.d("FAKE", "Body:" + new String(request.getBody(), "UTF-8"));
            }
        } catch (AuthFailureError e) {
            // cannot do anything
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return super.add(request);
    }
}