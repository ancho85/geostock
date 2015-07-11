package com.geotechpy.geostock.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.geotechpy.geostock.MainActivity;
import com.geotechpy.geostock.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Get Data from Main Server
 */
public class SyncFromServer {

    Context mContext;

    public SyncFromServer(Context context){
        this.mContext = context;
    }

    public void syncMasters() {
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, "please wait", "requesting data");

        String URL = "http://192.168.1.91/stocksurvey/RESTful/";
        JSONObject obj = new JSONObject();
        try {
            obj.put("all", "yes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(mContext);

        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
                Request.Method.POST, URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(mContext, "sync ok", Toast.LENGTH_LONG).show();
                        progressDialog.cancel();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.cancel();
                        String errorMsg = webServiceErrorParser(error);
                        Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }

    public static String webServiceErrorParser(VolleyError error) {
        String messageError = "";
        NetworkResponse response = error.networkResponse;
        if (response != null && response.data != null) {
            String json = new String(response.data);
            try {
                JSONObject responseWS = new JSONObject(json);
                JSONArray messagesObject = new JSONArray(responseWS.getString("messages"));
                for (int i = 0; i < messagesObject.length(); i++) {
                    JSONObject message = (JSONObject) messagesObject.get(i);
                    messageError += message.getString("dsc") + "\n";
                }
                if (messageError.length() <= 0) {
                    messageError = "unknown error";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (messageError.length() <= 0) {
            messageError = "ws comm error";
        }

        return messageError;
    }
}
