package com.geotechpy.geostock.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.geotechpy.geostock.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Generic Class to parse Volley Errors
 */
public class WSErrorParser {

    public static String webServiceErrorParser(Context mContext, VolleyError error) {
        StringBuilder messageError = new StringBuilder();
        messageError.append(mContext.getString(R.string.sync_wscomm_error));
        messageError.append("\n");
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
                    messageError.append("\n");
                }
            } catch (UnsupportedEncodingException e) {
                messageError.append(e.toString());
                messageError.append("\n");
            } catch (JSONException je) {
                messageError.append(je.getMessage());
                messageError.append("\n");
            }

        }
        if( error instanceof NetworkError) {
            messageError.append("Network Error\n");
            messageError.append(error.getMessage());
        } else if( error instanceof ServerError) {
            messageError.append("Server Error\n");
            messageError.append(error.getMessage());
        } else if( error instanceof AuthFailureError) {
            messageError.append("Auth Failure Error\n");
            messageError.append(error.getMessage());
        } else if( error instanceof ParseError) {
            messageError.append("Parse Error\n");
            messageError.append(error.getMessage());
        } else if( error instanceof TimeoutError) {
            messageError.append("Timeout Error\n");
            messageError.append(error.getMessage());
        }
        error.printStackTrace();

        return messageError.toString();
    }

}
