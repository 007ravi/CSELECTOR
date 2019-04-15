package com.ravi.cslogin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Network {

    private OkHttpClient mClient = new OkHttpClient();
    private  String mApiToken;

    private static  final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final Network ourInstance = new Network();

    public static Network getInstance() {
        return ourInstance;
    }

    private  Network() {
        mApiToken = "";
    }

    public void setApiToken(String apiToken) {
        mApiToken = apiToken;
    }

    public String getApiToken() {
        return mApiToken;
    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return  isAvailable;
    }

    public void makeApiGetRequest(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .header("token",getApiToken())
                .build();

        Call call = mClient.newCall(request);
        call.enqueue(callback);
    }

    public void makeApiPostRequest(String url,String json, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("token",getApiToken())
                .build();
        Call call = mClient.newCall(request);
        call.enqueue(callback);
    }
}
