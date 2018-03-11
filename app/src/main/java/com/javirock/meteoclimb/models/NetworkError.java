package com.javirock.meteoclimb.models;

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
