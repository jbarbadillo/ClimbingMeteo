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

    public static Response syncRequest(int method, Request request) throws IOException {
        Response response = null;
        switch (method){
            case GET: {
                response = syncGet(request);
                break;
            }
            case POST: {
                //response syncPost();
                break;
            }
        }

        return response;
    }
    public static void asyncRequest(int method, Request request, Callback callback){
        switch (method){
            case GET: {
                asyncGet(request, callback);
                break;
            }
            case POST: {
                asyncPost();
                break;
            }
        }
        httpClient.newCall(request).enqueue(callback);
    }
    private static Response syncGet(Request request) throws IOException{
        try (Response response = getHttpClient().newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response;
        }

    }
    /*
    private Response syncPost(){

    }*/
    private static void asyncGet(Request request, Callback callback){
        getHttpClient().newCall(request).enqueue(callback);
    }
    private static void asyncPost(){

    }


}
