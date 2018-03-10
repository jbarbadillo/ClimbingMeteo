package com.javirock.meteoclimb.models;


import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by javier on 28/02/2018.
 */

public class ApiNetwork {
    private static final String ENDPOINT = "https://api.github.com/repos/square/okhttp/contributors";
    private static final String AUTH_BEARER = "";

    private static Callback isAliveCallback = new Callback(){
        @Override
        public void onResponse(Call call, Response response) throws IOException{
            try (ResponseBody responseBody = response.body()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    Log.i("ApiNetwork",responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                Log.i("ApiNetwork", "Code: " + response.code());
                Log.i("ApiNetwork", responseBody.string());
            }
        }
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }
    };
    public static void isAlive(){
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .method("GET", null)
                .build();
        HttpClient.asyncRequest(HttpMethod.GET, request, isAliveCallback);
    }
}
