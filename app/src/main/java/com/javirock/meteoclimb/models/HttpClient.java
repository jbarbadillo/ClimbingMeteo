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

import static com.javirock.meteoclimb.models.HttpMethod.GET;
import static com.javirock.meteoclimb.models.HttpMethod.POST;


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

    public static void syncRequest(HttpMethod method, Request request){
        switch (method.hashCode()){
            case GET: {
                syncGet();
                break;
            }
            case POST: {
                syncPost();
                break;
            }
        }
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }
    public static void asyncRequest(HttpMethod method, Request request, Callback callback){
        switch (method.hashCode()){
            case GET: {
                asyncGet();
                break;
            }
            case POST: {
                asyncPost();
                break;
            }
        }
        httpClient.newCall(request).enqueue(callback);
    }
    private Response syncGet(){

    }
    private Response syncPost(){

    }
    private void asyncGet(){

    }
    private void asyncPost(){

    }


}
