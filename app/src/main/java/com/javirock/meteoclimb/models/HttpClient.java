package com.javirock.meteoclimb.models;

import java.io.File;
import java.io.IOError;
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

    public static Response performSimpleRequest(HttpRequest request){
        Request okHttpRequest;
        Response okHttpResponse = null;
        try{
            Request.Builder builder = new Request.Builder().url(request.getUrl());
            //addHeaders(builder, request);
            RequestBody requestBody = null;
            switch (request.getMethod()) {
                case GET: {
                    builder = builder.get();
                    break;
                }
                case POST: {
                    requestBody = request.getRequestBody();
                    builder = builder.post(requestBody);
                    break;
                }
                case PUT: {
                    requestBody = request.getRequestBody();
                    builder = builder.put(requestBody);
                    break;
                }
            }
            okHttpRequest = builder.build();
            if (request.getOkHttpClient() != null) {
                request.setCall(request.getOkHttpClient().newBuilder().cache(httpClient.cache()).build().newCall(okHttpRequest));
            } else {
                request.setCall(httpClient.newCall(okHttpRequest));
            }
            okHttpResponse = request.getCall().execute();            

        }catch (IOException e) {
            e.printStackTrace();
        }
        return okHttpResponse;
    }
    /*public static void addHeaders(Request.Builder builder, HttpRequest request) {
        if (request.getUserAgent() != null) {
            builder.addHeader(ANConstants.USER_AGENT, request.getUserAgent());
        } else if (sUserAgent != null) {
            request.setUserAgent(sUserAgent);
            builder.addHeader(ANConstants.USER_AGENT, sUserAgent);
        }
        Headers requestHeaders = request.getHeaders();
        if (requestHeaders != null) {
            builder.headers(requestHeaders);
            if (request.getUserAgent() != null && !requestHeaders.names().contains(ANConstants.USER_AGENT)) {
                builder.addHeader(ANConstants.USER_AGENT, request.getUserAgent());
            }
        }
    }*/


    public static String get(String url) throws IOException{
        Request request = new Request.Builder()
                .url(url)
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
