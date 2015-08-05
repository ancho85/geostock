package com.geotechpy.geostock;


import android.content.Context;
import android.test.AndroidTestCase;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.geotechpy.geostock.network.WSErrorParser.webServiceErrorParser;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class WSErrorParserTest extends AndroidTestCase {

    Context ctx;
    StringBuilder messageError;


    @Override
    public void setUp() throws Exception {
        super.setUp();
        ctx = getContext();
        messageError = new StringBuilder();
        messageError.append(ctx.getString(R.string.sync_wscomm_error)).append("\n");
    }

    public void testWSErrorMessages() throws UnsupportedEncodingException {
        byte[] jsonMsg = "{\"messages\":[{'dsc':'wrong1'}, {'dsc':'wrong2'}]}".getBytes("UTF-8");
        NetworkResponse response = new NetworkResponse(200, jsonMsg, null, false, 1000L);
        VolleyError error = new VolleyError(response);
        String errorMsg = webServiceErrorParser(mContext, error);
        messageError.append("wrong1\n");
        messageError.append("wrong2\n");
        assertThat(messageError.toString(), is(equalTo(errorMsg)));
    }

    public void testWSErrorUnknown() throws UnsupportedEncodingException{
        byte[] jsonMsg = "{\"messages\":[]}".getBytes("UTF-8");
        NetworkResponse response = new NetworkResponse(200, jsonMsg, null, false, 1000L);
        VolleyError error = new VolleyError(response);
        String errorMsg = webServiceErrorParser(mContext, error);
        messageError.append(mContext.getString(R.string.sync_unknown_error)).append("\n");
        assertThat(messageError.toString(), is(equalTo(errorMsg)));
    }

    public void testWSErrorJsonException(){
        byte[] jsonMsg = "[]".getBytes();
        NetworkResponse response = new NetworkResponse(200, jsonMsg, null, false, 1000L);
        VolleyError error = new VolleyError(response);
        String errorMsg = webServiceErrorParser(mContext, error);
        messageError.append("Value [] of type org.json.JSONArray cannot be converted to JSONObject\n");
        assertThat(messageError.toString(), is(equalTo(errorMsg)));
    }

    public void testWSOtherErrors() throws UnsupportedEncodingException {
        NetworkResponse response = new NetworkResponse(200, "{'messages':[]}".getBytes(), null, false, 1000L);
        HashMap<Object, String> instances = new HashMap<>();
        instances.put(new NetworkError(response), "Network Error");
        instances.put(new ServerError(response), "Server Error");
        instances.put(new AuthFailureError(response), "Auth Failure Error");
        instances.put(new ParseError(response), "Parse Error");
        instances.put(new TimeoutError(), "Timeout Error");
        for (Map.Entry<Object, String> entry: instances.entrySet()){
            String errorMsg = webServiceErrorParser(mContext, (VolleyError) entry.getKey());
            messageError = new StringBuilder();
            messageError.append(ctx.getString(R.string.sync_wscomm_error)).append("\n");
            if (!entry.getValue().equals("Timeout Error")) {
                messageError.append(mContext.getString(R.string.sync_unknown_error)).append("\n");
            }
            messageError.append(entry.getValue()).append("\nnull");
            assertThat(messageError.toString(), is(equalTo(errorMsg)));
        }
    }

}
