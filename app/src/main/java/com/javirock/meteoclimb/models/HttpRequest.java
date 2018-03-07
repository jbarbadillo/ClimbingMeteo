package com.javirock.meteoclimb.models;


/**
 * Class to handle requests in a scalable way
 */
public class HttpRequest {
    private int mMethod;
    private int mRequestType;
    private String mUrl;
    private ResponseType mResponseType;

    public String getUrl() {
        String tempUrl = mUrl;
        for (HashMap.Entry<String, String> entry : mPathParameterMap.entrySet()) {
            tempUrl = tempUrl.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        HttpUrl.Builder urlBuilder = HttpUrl.parse(tempUrl).newBuilder();
        if (mQueryParameterMap != null) {
            Set<Map.Entry<String, List<String>>> entries = mQueryParameterMap.entrySet();
            for (Map.Entry<String, List<String>> entry : entries) {
                String name = entry.getKey();
                List<String> list = entry.getValue();
                if (list != null) {
                    for (String value : list) {
                        urlBuilder.addQueryParameter(name, value);
                    }
                }
            }
        }
        return urlBuilder.build().toString();
    }
}
