package com.geotechpy.geostock.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.geotechpy.geostock.R;
import com.geotechpy.geostock.adapters.StockAdapter;
import com.geotechpy.geostock.app.GeotechpyStockApp;
import com.geotechpy.geostock.database.StockManager;
import com.geotechpy.geostock.database.UserManager;
import com.geotechpy.geostock.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import static com.geotechpy.geostock.network.WSErrorParser.webServiceErrorParser;

/**
 * Store Data to Main Server
 */
public class SyncToServer {
    Context mContext;
    StockAdapter stockAdapter;
    ProgressDialog progressDialog;
    RequestQueue queue;
    String userName;
    int pendingRequests = 0;
    int stockSerNr = 0;
    int zoneCode = 0;


    public SyncToServer(Context context){
        this.mContext = context;
        this.queue = GeotechpyStockApp.getRequestQueue();
    }

    public void setUserName(String userName){
        this.userName = userName;
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
        increasePendingRequests();
        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
        request.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        queue.add(request);
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
                        User user = UserManager.load(mContext, userName);
                        StockManager smd = new StockManager(mContext);
                        smd.update(stockSerNr,
                                user.getType(),
                                mContext.getString(R.string.stock_confirmed), //Confirming line
                                user.getCode(),
                                zoneCode);
                        stockAdapter.populateStocks(userName);
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
            decreasePendingRequests();
            if (getPendingRequests() == 0){
                Toast.makeText(mContext, replyMsg, Toast.LENGTH_SHORT).show();
                cancelDialog();
            }
        }
    }

    public class VolleyErrorResponseListener implements Response.ErrorListener{

        @Override
        public void onErrorResponse(VolleyError error) {
            decreasePendingRequests();
            String errorMsg = webServiceErrorParser(mContext, error);
            Toast.makeText(mContext, errorMsg, Toast.LENGTH_LONG).show();
            if (getPendingRequests() == 0){
                cancelDialog();
            }
        }
    }
}
