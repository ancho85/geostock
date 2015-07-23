package com.geotechpy.geostock.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.geotechpy.geostock.R;
import com.geotechpy.geostock.app.GeotechpyStockApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Store Data to Main Server
 */
public class SyncToServer {
    Context mContext;
    ProgressDialog progressDialog;
    int pendingRequests = 0;
    RequestQueue queue;

    public SyncToServer(Context context){
        this.mContext = context;
        this.queue = GeotechpyStockApp.getRequestQueue();
    }

    public void syncStock() {
        showDialog();
        String stockURL = "http://geotechpy.com/inventario/ajax/inventarios/guardar_inventario.php";

        //stock request
        increasePendingRequests();
        JSONObject obj = new JSONObject();
        try {
            obj.put("key", "value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonArrayStockRequest = new JsonObjectRequest(Request.Method.POST,
                stockURL, obj, new StockSyncListener(), new VolleyErrorResponseListener());
        addToQueue(jsonArrayStockRequest, "STOCKSYNC");
    }

    public void addToQueue(JsonObjectRequest request, String tag){
        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        queue.add(request);
    }

    public String webServiceErrorParser(VolleyError error) {
        StringBuilder messageError = new StringBuilder();
        NetworkResponse response = error.networkResponse;
        if (response != null && response.data != null) {
            try {
                String json = new String(response.data, "UTF-8");
                JSONObject responseWS = new JSONObject(json);
                JSONArray messagesObject = new JSONArray(responseWS.getString("messages"));
                for (int i = 0; i < messagesObject.length(); i++) {
                    JSONObject message = (JSONObject) messagesObject.get(i);
                    messageError.append(message.getString("dsc"));
                    messageError.append("\n");
                }
                if (messageError.length() <= 0) {
                    messageError.append(mContext.getString(R.string.sync_unknown_error));
                }
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e)).toString();
            } catch (JSONException je) {
                return Response.error(new ParseError(je)).toString();
            }

        }
        if (messageError.length() <= 0) {
            messageError.append(mContext.getString(R.string.sync_wscomm_error));
            if( error instanceof NetworkError) {
                messageError.append("Network Error");
                messageError.append(error.getMessage());
            } else if( error instanceof ServerError) {
                messageError.append("Server Error");
                messageError.append(error.getMessage());
            } else if( error instanceof AuthFailureError) {
                messageError.append("Auth Failure Error");
                messageError.append(error.getMessage());
            } else if( error instanceof ParseError) {
                messageError.append("Parse Error");
                messageError.append(error.getMessage());
            } else if( error instanceof TimeoutError) {
                messageError.append("Timeout Error");
                messageError.append(error.getMessage());
            }
        }

        return messageError.toString();
    }

    public int getPendingRequests(){
        return pendingRequests;
    }

    public void increasePendingRequests(){
        pendingRequests++;
    }

    public void decreasePendingRequests(){
        pendingRequests--;
    }

    public void showDialog(){
        progressDialog = ProgressDialog.show(mContext, mContext.getString(R.string.sync_please_wait), mContext.getString(R.string.sync_sending_data));
    }

    public void updateDialogMessage(String message){
        progressDialog.setMessage(message);
    }

    public void cancelDialog(){
        progressDialog.dismiss();
    }

    public class StockSyncListener implements Response.Listener<JSONObject>{

        @Override
        public void onResponse(JSONObject response){
            updateDialogMessage(mContext.getString(R.string.sync_stock));
            decreasePendingRequests();
            if (getPendingRequests() == 0){
                Toast.makeText(mContext, R.string.db_sync, Toast.LENGTH_SHORT).show();
                cancelDialog();
            }
        }
    }

    public class VolleyErrorResponseListener implements Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            decreasePendingRequests();
            String errorMsg = webServiceErrorParser(error);
            Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
            if (getPendingRequests() == 0){
                cancelDialog();
            }
        }
    }
}
