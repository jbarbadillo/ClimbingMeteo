package com.javirock.meteoclimb.models;

import javax.security.auth.callback.Callback;

import okhttp3.Request;

/**
 * Created by javier on 28/02/2018.
 */

public class ApiNetwork {
    private static final String ENDPOINT = "https://api.github.com/repos/square/okhttp/contributors";
    private static final String AUTH_BEARER = "";

    private static Callback isAliveCallback = new Callback() {
        @Override
        public int hashCode() {
            return super.hashCode();
        }
    };
    public static void isAlive(){
        Request request = new Request.Builder()
                .url(ENDPOINT)
                .build();
        HttpClient.asyncRequest(HttpMethod.GET, request, isAliveCallback);
    }
}
