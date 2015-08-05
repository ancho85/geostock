package com.geotechpy.geostock.network.dev;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.StringRequest;
import com.geotechpy.geostock.network.GeotechpyApi;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Fake {@link HttpStack} that returns the fake content using resource file in res/raw.
 */

@SuppressWarnings("deprecation")
class FakeHttpStack implements HttpStack {
    private static final int SIMULATED_DELAY_MS = 500;
    private final Context context;

    FakeHttpStack(Context context) {
        this.context = context;
    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> stringStringMap)
            throws IOException, AuthFailureError {

        try {
            Thread.sleep(SIMULATED_DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HttpResponse response
                = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, "OK"));
        List<Header> headers = defaultHeaders();
        response.setHeaders(headers.toArray(new Header[headers.size()]));
        response.setLocale(Locale.ENGLISH);
        response.setEntity(createEntity(request));
        return response;
    }

    private List<Header> defaultHeaders() {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd mmm yyyy HH:mm:ss zzz", Locale.getDefault());
        return Lists.<Header>newArrayList(
                new BasicHeader("Date", dateFormat.format(new Date())),
                new BasicHeader("Server",
                        "Apache/1.3.42 (Unix) mod_ssl/2.8.31 OpenSSL/0.9.8e")
        );
    }

    private HttpEntity createEntity(Request request) throws UnsupportedEncodingException {
        String resourceName = constructFakeResponseFileName(request);
        HttpEntity httpEntity = new StringEntity("[{\"a\":1,\"b\":2,\"c\":3,\"d\":4,\"e\":5}]"); //default response
        InputStream stream;
        int resourceId = context.getResources().getIdentifier(
                resourceName, "raw", context.getApplicationContext().getPackageName());
        if (resourceId == 0) {
            Log.w("FAKE", "No fake file named " + resourceName +
                    " default fake response should be used.");
        } else {
            stream = context.getResources().openRawResource(resourceId);
            try {
                String string = CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
                if ("randomInt".equals(string)) {
                    string = Integer.toString(new Random().nextInt(Integer.MAX_VALUE));
                }
                httpEntity = new StringEntity(string);
            } catch (IOException e) {
                Log.d("FAKE", "error reading " + resourceName + ". Error: " + e);
            }
            finally{
                if (stream != null){
                    try{
                        stream.close();
                    }catch (IOException e){
                        Log.d("FAKE", "error closing stream" + ". Error: " + e);
                    }

                }
            }
        }

        if (request instanceof StringRequest) {
            httpEntity = new StringEntity("100");
        }
        return httpEntity;
    }

    private String constructFakeResponseFileName(Request request) {
        String reqUrl = request.getUrl();
        String apiName = reqUrl.substring(GeotechpyApi.BASEURL.length()).split("/", 2)[0];
        return "fake_res_" + apiName;
    }
}