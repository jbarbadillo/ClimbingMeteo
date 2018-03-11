package com.javirock.meteoclimb.models;

/**
 * Created by javier on 11/03/2018.
 */

public class NetworkError {
    private int errorCode;
    private String message;

    public NetworkError(int code, String message){
        this.errorCode = code;
        this.message = message;
    }
    int getErrorCode(){
        return this.errorCode;
    }
    String getMessage(){
        return this.message;
    }
}
