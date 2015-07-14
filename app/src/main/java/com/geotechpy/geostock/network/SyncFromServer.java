package com.geotechpy.geostock.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

    public SyncFromServer(Context context){
        this.mContext = context;
    }

    public void syncMasters() {
        final ProgressDialog progressDialog = ProgressDialog.show(mContext, mContext.getString(R.string.sync_please_wait), mContext.getString(R.string.sync_requesting_data));

        String URL = "http://jsonplaceholder.typicode.com/users";
        JSONObject obj = new JSONObject();
        try {
            obj.put("key", "value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = GeotechpyStockApp.getRequestQueue();
        //RequestQueue queue = Volley.newRequestQueue(mContext);

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
}
