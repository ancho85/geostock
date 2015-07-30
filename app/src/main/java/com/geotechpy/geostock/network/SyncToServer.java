package com.geotechpy.geostock.network;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.geotechpy.geostock.R;
import com.geotechpy.geostock.adapters.StockAdapter;
import com.geotechpy.geostock.app.GeotechpyStockApp;
import com.geotechpy.geostock.database.StockDetailManager;
import com.geotechpy.geostock.database.StockManager;
import com.geotechpy.geostock.models.StockDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.geotechpy.geostock.app.GeotechpyStockApp.displayMessage;
import static com.geotechpy.geostock.network.WSErrorParser.webServiceErrorParser;

/**
 * Store Data to Main Server
 */
public class SyncToServer {
    Context mContext;
    StockAdapter stockAdapter;
    ProgressDialog progressDialog;
    int pendingRequests = 0;
    int stockSerNr = 0;
    int zoneCode = 0;


    public SyncToServer(Context context){
        this.mContext = context;
    }

    public void setStockAdapter(StockAdapter adapter){
        this.stockAdapter = adapter;
    }

    public void setStockSerNr(int serNr){
        this.stockSerNr = serNr;
    }

    public void setZoneCode(int code){
        this.zoneCode = code;
    }


    public void syncStock() {
        showDialog();
        String stockURL = "http://geotechpy.com/inventario/ajax/inventario/save_inventario.php";

        //stock request
        StockDetailManager stockDetailManager = new StockDetailManager(mContext);
        ArrayList<StockDetail> al_stockDetail = stockDetailManager.getStockDetails(stockSerNr);
        List<Map<String,String>> paramsList =  new ArrayList<>();
        for (int i=0; i < al_stockDetail.size(); i++) {
            HashMap<String, String> params = new HashMap<>();
            params.put("zona_codigo", String.valueOf(zoneCode));
            params.put("usua_codigo", GeotechpyStockApp.getUserName());
            params.put("inve_tipo", GeotechpyStockApp.getStockType());
            params.put("prod_codigo", al_stockDetail.get(i).getItem_code());
            params.put("invd_cantidad", al_stockDetail.get(i).getQty().toString());
            paramsList.add(params);
        }
        JSONObject json = new JSONObject();
        try {
            json.put("json", String.valueOf(new JSONArray(paramsList)));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonStockRequest = new JsonObjectRequest(Request.Method.POST,
                stockURL, json,
                new StockSyncListener(),
                new VolleyErrorResponseListener());
        addToQueue(jsonStockRequest, "STOCKSYNC");
    }

    public void addToQueue(JsonObjectRequest request, String tag){
        increasePendingRequests();
        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        GeotechpyStockApp.addToRequestQueue(request);
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
            String replyMsg = mContext.getString(R.string.db_sync);
            if (response.has("status")){
                try {
                    if (response.getString("status").equals("ok")){
                        StockManager smd = new StockManager(mContext);
                        smd.update(stockSerNr,
                                GeotechpyStockApp.getStockType(),
                                mContext.getString(R.string.stock_confirmed), //Confirming line
                                GeotechpyStockApp.getUserName(),
                                zoneCode);
                        stockAdapter.populateStocks();
                    }else{
                        replyMsg = response.getString("status");
                    }
                } catch (JSONException e) {
                    replyMsg = e.getMessage();
                    e.printStackTrace();
                }
            }else{
                replyMsg = response.toString();
            }
            checkQueue(replyMsg);
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
