package com.javirock.meteoclimb.models;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Class to handle requests in a scalable way
 */
public class HttpRequest {
    private int mMethod;
    private int mRequestType;
    private String mUrl;
    //private ResponseType mResponseType;
    private HashMap<String, List<String>> mHeadersMap = new HashMap<>();
    private MediaType customMediaType = null;
    private HashMap<String, String> mBodyParameterMap = new HashMap<>();
    private HashMap<String, String> mUrlEncodedFormBodyParameterMap = new HashMap<>();
    private String mApplicationJsonString = null;
    private static final MediaType JSON_MEDIA_TYPE =
            MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient mOkHttpClient = null;
    private Call call;

    public HttpRequest(GetRequestBuilder builder){

    }
    public HttpRequest(PostRequestBuilder builder){

    }
    public static class PostRequestBuilder{
        private int mMethod = Method.POST;
        private String mUrl;
        public PostRequestBuilder(String url) {
            this.mUrl = url;
        }
    }
    public static class GetRequestBuilder{
        private int mMethod = Method.GET;
        private String mUrl;
        public GetRequestBuilder(String url, int method) {
            this.mUrl = url;
        }
    }
    /**
     * Gets the endpoint url for this request
     *
     * @return Endpoint url
     */
    public String getUrl() {
        return mUrl;
    }
    public int getMethod() {
        return mMethod;
    }
    public int getRequestType() {
        return mRequestType;
    }
    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
    public Call getCall() {
        return call;
    }
    public void setCall(Call call) {
        this.call = call;
    }
    public Headers getHeaders() {
        Headers.Builder builder = new Headers.Builder();
        try {
            if (mHeadersMap != null) {
                Set<Map.Entry<String, List<String>>> entries = mHeadersMap.entrySet();
                for (Map.Entry<String, List<String>> entry : entries) {
                    String name = entry.getKey();
                    List<String> list = entry.getValue();
                    if (list != null) {
                        for (String value : list) {
                            builder.add(name, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.build();
    }
    public RequestBody getRequestBody() {
        if (mApplicationJsonString != null) {
            if (customMediaType != null) {
                return RequestBody.create(customMediaType, mApplicationJsonString);
            }
            return RequestBody.create(JSON_MEDIA_TYPE, mApplicationJsonString);
        }  else {
            FormBody.Builder builder = new FormBody.Builder();
            try {
                for (HashMap.Entry<String, String> entry : mBodyParameterMap.entrySet()) {
                    builder.add(entry.getKey(), entry.getValue());
                }
                for (HashMap.Entry<String, String> entry : mUrlEncodedFormBodyParameterMap.entrySet()) {
                    builder.addEncoded(entry.getKey(), entry.getValue());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return builder.build();
        }
    }
    public interface Method {
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
        int HEAD = 4;
        int PATCH = 5;
        int OPTIONS = 6;
    }
}
