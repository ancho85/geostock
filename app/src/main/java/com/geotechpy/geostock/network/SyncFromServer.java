package com.geotechpy.geostock.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.geotechpy.geostock.R;
import com.geotechpy.geostock.app.GeotechpyStockApp;
import com.geotechpy.geostock.database.ItemManager;
import com.geotechpy.geostock.database.StockDetailManager;
import com.geotechpy.geostock.database.StockManager;
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

    public SyncFromServer(Context context){
        this.mContext = context;
    }

    public void syncMasters() {
        showDialog();
        String URL = "http://nothing";//"http://jsonplaceholder.typicode.com/users";
        //https://geotechpy.com/inventario/ajax/productos/get_full_productos_rest.php
        //https://geotechpy.com/inventario/ajax/usuarios/get_full_usuarios_rest.php
        //https://geotechpy.com/inventario/ajax/zonas/get_full_zonas_rest.php
        JSONObject obj = new JSONObject();
        try {
            obj.put("key", "value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tv_mainStatus = (TextView) ((AppCompatActivity) mContext).findViewById(R.id.tv_mainstatus);
        tv_mainStatus.setText("Sync started...");

        RequestQueue queue = GeotechpyStockApp.getRequestQueue();
        increasePendingRequests();
        updateDialogMessage("Sync users");
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.POST, URL, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UserManager um = new UserManager(mContext);
                        um.insert("ancho", "666", mContext.getString(R.string.zone_deposit));
                        um.insert("alex", "777", mContext.getString(R.string.zone_lab));
                        um.insert("liz", "liz", mContext.getString(R.string.zone_both));

                        ZoneManager zm = new ZoneManager(mContext);
                        zm.insert(1, "Deposit Nr. 1", mContext.getString(R.string.zone_deposit));
                        zm.insert(2, "Deposit Nr. 2", mContext.getString(R.string.zone_deposit));
                        zm.insert(3, "Deposit Nr. 3", mContext.getString(R.string.zone_deposit));
                        zm.insert(4, "Lab Nr. 1", mContext.getString(R.string.zone_lab));
                        zm.insert(5, "Lab Nr. 2", mContext.getString(R.string.zone_lab));

                        ItemManager it = new ItemManager(mContext);
                        it.insert("keyboard", "Keyboard", mContext.getString(R.string.zone_deposit));
                        it.insert("engine", "Fusion Engine", mContext.getString(R.string.zone_lab));
                        it.insert("quantum", "Quantum Engine", mContext.getString(R.string.zone_lab));

                        StockManager sm = new StockManager(mContext);
                        sm.insert(1, mContext.getString(R.string.zone_deposit), mContext.getString(R.string.stock_active), "ancho", 1);
                        sm.insert(2, mContext.getString(R.string.zone_deposit), mContext.getString(R.string.stock_confirmed), "ancho", 3);
                        sm.insert(3, mContext.getString(R.string.zone_lab), mContext.getString(R.string.stock_confirmed), "alex", 4);

                        StockDetailManager sdm = new StockDetailManager(mContext);
                        sdm.insert(1, "keyboard", 10f);
                        sdm.insert(1, "engine", 20f);
                        sdm.insert(2, "keyboard", 30f);
                        sdm.insert(3, "engine", 40f);
                        sdm.insert(3, "quantum", 50f);

                        progressDialog.cancel();
                        Toast.makeText(mContext, R.string.db_sync, Toast.LENGTH_SHORT).show();
                        tv_mainStatus.setText(R.string.db_sync);
                    }
                }, new VolleyErrorResponse()
        );
        queue.add(jsonArrayRequest);
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
    
    public class VolleyErrorResponse implements Response.ErrorListener{

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
