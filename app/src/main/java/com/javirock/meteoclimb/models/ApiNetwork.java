package com.javirock.meteoclimb.models;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.JsonReader;
import android.util.Log;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonQualifier;
import com.squareup.moshi.Moshi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ApiNetwork {
    private static final String URL = "https://opendata.aemet.es/opendata/api/";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYXZpZXIuYmFyYmFkaWxsb0BnbWFpbC5jb20iLCJqdGkiOiJjZmJhN2U3Ny05YWY5LTQ5ZWEtYTU5NC04ZGE0Mjc3NTZlNmQiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTUyMDcxMjcwMywidXNlcklkIjoiY2ZiYTdlNzctOWFmOS00OWVhLWE1OTQtOGRhNDI3NzU2ZTZkIiwicm9sZSI6IiJ9.ryUk5Wljb7pmcfxJVyztO_tyzyPdv5peCaKRG5lpM_Y";

    private static ICallback mDelegate = null;
    public interface ICallback{
        void dailyData(JSONObject json, NetworkError error);
        void hourlyData(JSONObject json, NetworkError error);
    }
    public static void setDelegate(ICallback context){

        mDelegate = context;
    }
    private static Callback isAliveCallback = new Callback(){
        @Override
        public void onResponse(Call call, Response response) throws IOException{
            try (ResponseBody responseBody = response.body()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    Log.i("ApiNetwork",responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                Log.i("ApiNetwork", "Code: " + response.code());
                //Log.i("ApiNetwork", responseBody.string());
            }
        }
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }
    };
    private static Callback predictionDayCallback = new Callback(){
        @Override
        public void onResponse(Call call, Response response) throws IOException{
            try(ResponseBody responseBody = response.body()){
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                Log.i("ApiNetwork", "Code: " + response.code());


                try {
                    JSONObject json = new JSONObject(responseBody.string());
                    String url = json.getString("datos");
                    getData(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
            if(e.getMessage().contains("CertPathValidatorException")){
                Log.i("ApiNetwork", "Ignoring Failure: " + e.getMessage());
            }else{
                Log.i("ApiNetwork", "predictionDayCallback Failure: " + e.getMessage());
            }
        }
    };
    private static Callback predictionHoursCallback = new Callback(){
        @Override
        public void onResponse(Call call, Response response) throws IOException{
            try(ResponseBody responseBody = response.body()){
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                Log.i("ApiNetwork", "Code: " + response.code());


            }

        }
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }
    };
    private static Callback getDataCallback = new Callback(){
        @Override
        public void onResponse(Call call, Response response) throws IOException{
            try(ResponseBody responseBody = response.body()){
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                Log.i("ApiNetwork", "Code: " + response.code());


                try {
                    JSONArray json = new JSONArray(responseBody.string());
                    JSONObject predictions = new JSONObject(json.get(0).toString());
                    JSONObject prediction = new JSONObject(predictions.getString("prediccion"));
                    JSONArray dias = new JSONArray(prediction.getString("dia"));
                    JSONObject dia = new JSONObject(dias.get(0).toString());
                    JSONArray precipitaciones = new JSONArray(dia.getString("probPrecipitacion"));
                    Log.i("ApiNetwork", "probPrecipitacion: " + precipitaciones.toString());
                    mDelegate.dailyData(dia,null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();

            if(e.getMessage().contains("CertPathValidatorException")){
                Log.i("ApiNetwork", "Ignoring Failure: " + e.getMessage());
            }else{
                Log.i("ApiNetwork", "getDataCallback Failure: " + e.getMessage());
            }
        }
    };

    public static  void getData(String url){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json; q=0.5")
                .method("GET", null)
                .build();
        HttpClient.asyncRequest(HttpMethod.GET, request, getDataCallback);
    }
    public static void isAlive(){
        Request request = new Request.Builder()
                .url("https://www.remote.com/api/vo/isAlive")
                .method("GET", null)
                .build();
        HttpClient.asyncRequest(HttpMethod.GET, request, isAliveCallback);
    }
    public static   JSONObject getSpainTowns(String location) throws IOException {
        Request request = new Request.Builder()
                .url(URL+Endpoints.MUNICIPIOS)
                .method("GET", null)
                .build();
        HttpClient.syncRequest(HttpMethod.GET, request);

        //Parse response un JSON
        return null;
    }
    public static  void getTownPredictionHours(String id) throws IOException {
        Request request = new Request.Builder()
                .url(URL+Endpoints.MUNICIPIO_HORARIA+"/"+id)
                .addHeader("API_KEY", API_KEY)
                .method("GET", null)
                .build();

        Log.i("ApiNetwork", "Request: " + request.toString());
        HttpClient.asyncRequest(HttpMethod.GET, request, predictionHoursCallback);

    }
    public static  void getTownPredictionDay(String id) throws IOException {
        Request request = new Request.Builder()
                .url(URL+Endpoints.MUNICIPIO_DIARIA+id)
                .addHeader("API_KEY", API_KEY)
                .method("GET", null)
                .build();
        Log.i("ApiNetwork", "Request: " + request.toString());
        HttpClient.asyncRequest(HttpMethod.GET, request, predictionDayCallback);

    }
    //Example code
    private static final MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg");

    public static void genericRequest(String text, Bitmap image){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("text", text)
                .addFormDataPart("image", "bitmap.jpeg",
                        RequestBody.create(MEDIA_TYPE_JPEG, byteArray))
                .build();
        Request request = new Request.Builder()
                .url(URL+"endpoint")
                .addHeader("API_KEY", API_KEY)
                .post(requestBody)
                .build();
        HttpClient.asyncRequest(HttpMethod.POST, request, genericCallback);
    }
    private static Callback genericCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try(ResponseBody responseBody = response.body()){
                if (!response.isSuccessful()){
                    // Process error code and message
                }else{
                    // Process successful response
                }
            }
        }
    };

}
