package com.javirock.meteoclimb.models;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpClient {
    public static OkHttpClient httpClient = getHttpClient();

    /**
     * Gets an instance of the client
     * @return An okhttp client instance
     */
    public static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            return getDefaultClient();
        }
        return httpClient;
    }
    public static OkHttpClient getDefaultClient() {
        return new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    
    public static void asyncGet(Request request, Callback callback){
        httpClient.newCall(request).enqueue(callback);
    }
    public static String syncGet(Request request) throws IOException{
        Response response = httpClient.newCall(request).execute();
        return response.body().string();

    }

    public static String syncPostMultipart(Request request) throws IOException {
        /*RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();*/
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }
    public static void put(){

    }
}
