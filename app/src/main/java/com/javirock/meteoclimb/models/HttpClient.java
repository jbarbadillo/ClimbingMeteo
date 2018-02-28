package com.javirock.meteoclimb.models;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpClient {
    public final static OkHttpClient client = new OkHttpClient(); // TODO: use a builder?
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    
    public static void get(){

    }
    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
    public static void put(){

    }
}
