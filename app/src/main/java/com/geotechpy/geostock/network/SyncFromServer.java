package com.geotechpy.geostock.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
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
import com.geotechpy.geostock.database.ItemManager;
import com.geotechpy.geostock.database.UserManager;
import com.geotechpy.geostock.database.ZoneManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Get Data from Main Server
 */
public class SyncFromServer {

    Context mContext;
    ProgressDialog progressDialog;
    TextView tv_mainStatus;
    int pendingRequests = 0;
    RequestQueue queue;

    public SyncFromServer(Context context){
        this.mContext = context;
        this.queue = GeotechpyStockApp.getRequestQueue();
    }

    public void syncMasters() {
        showDialog();
        String itemURL = "http://geotechpy.com/inventario/ajax/productos/get_full_productos_rest.php";
        String userURL = "http://geotechpy.com/inventario/ajax/usuarios/get_full_usuarios_rest.php";
        String zoneURL = "http://geotechpy.com/inventario/ajax/zonas/get_full_zonas_rest.php";

        tv_mainStatus = (TextView) ((AppCompatActivity) mContext).findViewById(R.id.tv_mainstatus);
        tv_mainStatus.setText(mContext.getString(R.string.sync_started));

        //user request
        increasePendingRequests();
        JsonObjectRequest jsonArrayUserRequest = new JsonObjectRequest(Request.Method.GET,
                userURL, new JSONObject(), new UserSyncListener(), new VolleyErrorResponseListener());
        addToQueue(jsonArrayUserRequest, "USERSYNC");

        //zone request
        increasePendingRequests();
        JsonObjectRequest jsonArrayZoneRequest = new JsonObjectRequest(Request.Method.GET,
                zoneURL, new JSONObject(), new ZoneSyncListener(), new VolleyErrorResponseListener());
        addToQueue(jsonArrayZoneRequest, "ZONESYNC");

        //item request
        increasePendingRequests();
        JsonObjectRequest jsonArrayItemRequest = new JsonObjectRequest(Request.Method.GET,
                itemURL, new JSONObject(), new ItemSyncListener(), new VolleyErrorResponseListener());
        addToQueue(jsonArrayItemRequest, "ITEMSYNC");
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
        progressDialog = ProgressDialog.show(mContext, mContext.getString(R.string.sync_please_wait), mContext.getString(R.string.sync_requesting_data));
    }

    public void updateDialogMessage(String message){
        progressDialog.setMessage(message);
    }

    public void cancelDialog(){
        progressDialog.dismiss();
    }

    public class UserSyncListener implements Response.Listener<JSONObject>{

        @Override
        public void onResponse(JSONObject response){
            updateDialogMessage(mContext.getString(R.string.sync_users));
            try {
                JSONArray jsonArray = response.getJSONArray("");
                for(int index = 0 ; index < jsonArray.length(); index++) {
                    JSONArray userArray = jsonArray.getJSONArray(index);
                    UserManager um = new UserManager(mContext);
                    um.insert(userArray.get(0).toString(), userArray.get(1).toString(), userArray.get(2).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            decreasePendingRequests();
            if (getPendingRequests() == 0){
                Toast.makeText(mContext, R.string.db_sync, Toast.LENGTH_SHORT).show();
                tv_mainStatus.setText(R.string.db_sync);
                cancelDialog();
            }
        }
    }

    public class ZoneSyncListener implements Response.Listener<JSONObject>{

        @Override
        public void onResponse(JSONObject response) {
            updateDialogMessage(mContext.getString(R.string.sync_zones));
            try {
                JSONArray jsonArray = response.getJSONArray("");
                for(int index = 0 ; index < jsonArray.length(); index++) {
                    JSONArray zoneArray = jsonArray.getJSONArray(index);
                    ZoneManager zm = new ZoneManager(mContext);
                    zm.insert(Integer.valueOf(zoneArray.get(0).toString()), zoneArray.get(1).toString(), zoneArray.get(2).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            decreasePendingRequests();
            if (getPendingRequests() == 0){
                Toast.makeText(mContext, R.string.db_sync, Toast.LENGTH_SHORT).show();
                tv_mainStatus.setText(R.string.db_sync);
                cancelDialog();
            }
        }
    }

    public class ItemSyncListener implements Response.Listener<JSONObject>{

        @Override
        public void onResponse(JSONObject response) {
            updateDialogMessage(mContext.getString(R.string.sync_items));
            try {
                JSONArray jsonArray = response.getJSONArray("");
                for(int index = 0 ; index < jsonArray.length(); index++) {
                    JSONArray itemArray = jsonArray.getJSONArray(index);
                    ItemManager it = new ItemManager(mContext);
                    it.insert(itemArray.get(0).toString(), itemArray.get(1).toString(), itemArray.get(2).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            decreasePendingRequests();
            if (getPendingRequests() == 0){
                Toast.makeText(mContext, R.string.db_sync, Toast.LENGTH_SHORT).show();
                tv_mainStatus.setText(R.string.db_sync);
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
            tv_mainStatus.setText(errorMsg);
            if (getPendingRequests() == 0){
                cancelDialog();
            }
        }
    }
}
