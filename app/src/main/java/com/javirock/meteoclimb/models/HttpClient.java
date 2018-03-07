package com.javirock.meteoclimb.models;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

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



    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");



    public static String get(String url) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = httpClient.newCall(request).execute();
        return response.body().string();

    }
    public static String post(String url, String json) throws IOException {
        // TODO: build body from json
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email", "jbarbadillo@gmail.com")
                .addFormDataPart("image", "logo-square.png",
                        RequestBody.create(MEDIA_TYPE_JPEG, new File("website/static/logo-square.png")))
                .build();

        Request request = new Request.Builder()
                //.header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url(url)
                .post(body)
                .build();

        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }
    public static String postMultipart(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }
    public static void put(){

    }
}
