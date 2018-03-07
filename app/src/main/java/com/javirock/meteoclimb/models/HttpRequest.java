package com.javirock.meteoclimb.models;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.HttpUrl;

/**
 * Class to handle requests in a scalable way
 */
public class HttpRequest {
    private int mMethod;
    private int mRequestType;
    private String mUrl;
    private ResponseType mResponseType;

    /**
     * Gets the endpoint url for this request
     *
     * @return Endpoint url
     */
    public String getUrl() {
        return mUrl;
    }
}
