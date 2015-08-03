package com.geotechpy.geostock.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.geotechpy.geostock.R;
import com.geotechpy.geostock.app.GeotechpyStockApp;
import com.geotechpy.geostock.database.ItemManager;
import com.geotechpy.geostock.database.UserManager;
import com.geotechpy.geostock.database.ZoneManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.geotechpy.geostock.app.GeotechpyStockApp.displayMessage;
import static com.geotechpy.geostock.network.WSErrorParser.webServiceErrorParser;

/**
 * Get Data from Main Server
 */
public class SyncFromServer {

    Context mContext;
    ProgressDialog progressDialog;
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

        //user request
        JsonArrayRequest jsonArrayUserRequest = new JsonArrayRequest(Request.Method.GET,
                userURL, new JSONObject(), new UserSyncListener(), new VolleyErrorResponseListener());
        addToQueue(jsonArrayUserRequest, "USERSYNC");

        //zone request
        JsonArrayRequest jsonArrayZoneRequest = new JsonArrayRequest(Request.Method.GET,
                zoneURL, new JSONObject(), new ZoneSyncListener(), new VolleyErrorResponseListener());
        addToQueue(jsonArrayZoneRequest, "ZONESYNC");

        //item request
        JsonArrayRequest jsonArrayItemRequest = new JsonArrayRequest(Request.Method.GET,
                itemURL, new JSONObject(), new ItemSyncListener(), new VolleyErrorResponseListener());
        addToQueue(jsonArrayItemRequest, "ITEMSYNC");
    }

    public void addToQueue(JsonArrayRequest request, String tag){
        increasePendingRequests();
        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        queue.add(request);
    }

    public void checkQueue(String endMsg){
        decreasePendingRequests();
        if (getPendingRequests() == 0){
            displayMessage(endMsg);
            cancelDialog();
        }
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

    public class UserSyncListener implements Response.Listener<JSONArray>{

        @Override
        public void onResponse(JSONArray response){
            updateDialogMessage(mContext.getString(R.string.sync_users));
            try {
                for(int index = 0 ; index < response.length(); index++) {
                    JSONArray userArray = response.getJSONArray(index);
                    UserManager um = new UserManager(mContext);
                    um.insert(userArray.get(0).toString(), userArray.get(1).toString(), userArray.get(2).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            checkQueue(mContext.getString(R.string.db_sync));
        }
    }

    public class ZoneSyncListener implements Response.Listener<JSONArray>{

        @Override
        public void onResponse(JSONArray response) {
            updateDialogMessage(mContext.getString(R.string.sync_zones));
            try {
                for(int index = 0 ; index < response.length(); index++) {
                    JSONArray zoneArray = response.getJSONArray(index);
                    ZoneManager zm = new ZoneManager(mContext);
                    zm.insert(Integer.valueOf(zoneArray.get(0).toString()), zoneArray.get(1).toString(), zoneArray.get(2).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            checkQueue(mContext.getString(R.string.db_sync));
        }
    }

    public class ItemSyncListener implements Response.Listener<JSONArray>{

        @Override
        public void onResponse(JSONArray response) {
            updateDialogMessage(mContext.getString(R.string.sync_items));
            try {
                String code;
                String name;
                Long barCode;
                String type;
                String tmpBarCode;
                for(int index = 0 ; index < response.length(); index++) {
                    JSONArray itemArray = response.getJSONArray(index);
                    ItemManager it = new ItemManager(mContext);
                    code = getNotNullString(itemArray.get(0).toString());
                    name = getNotNullString(itemArray.get(1).toString());
                    tmpBarCode = getNotNullString(itemArray.get(2).toString());
                    if (tmpBarCode.equals("")){
                        tmpBarCode = "0";
                    }
                    barCode = Long.valueOf(tmpBarCode);
                    type = getNotNullString(itemArray.get(3).toString());
                    it.insert(code, name, barCode, type);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            checkQueue(mContext.getString(R.string.db_sync));
        }

        public String getNotNullString(String string){
            String result = "";
            if (!string.equals("null")){
                result = string;
            }
            return result;
        }
    }

    public class VolleyErrorResponseListener implements Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            String errorMsg = webServiceErrorParser(mContext, error);
            checkQueue(errorMsg);
        }
    }
}
