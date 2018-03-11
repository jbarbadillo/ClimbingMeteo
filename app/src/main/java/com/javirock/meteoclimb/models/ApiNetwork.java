package com.javirock.meteoclimb.models;


import android.util.JsonReader;
import android.util.Log;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonQualifier;
import com.squareup.moshi.Moshi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ApiNetwork {
    private static final String URL = "https://opendata.aemet.es/opendata/api/";
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYXZpZXIuYmFyYmFkaWxsb0BnbWFpbC5jb20iLCJqdGkiOiJjZmJhN2U3Ny05YWY5LTQ5ZWEtYTU5NC04ZGE0Mjc3NTZlNmQiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTUyMDcxMjcwMywidXNlcklkIjoiY2ZiYTdlNzctOWFmOS00OWVhLWE1OTQtOGRhNDI3NzU2ZTZkIiwicm9sZSI6IiJ9.ryUk5Wljb7pmcfxJVyztO_tyzyPdv5peCaKRG5lpM_Y";

    private final Moshi moshi = new Moshi.Builder().build();

    private final JsonAdapter<Data> gistJsonAdapter = moshi.adapter(Data.class);

    static class Data{
        public List<Answer> answer;
    }
    static class Answer{
        Map<String, Day> prediccion;
    }
    static class Day{
        List<Values> values ;
    }
    static  class Values{
        List<String> estadoCielo;
    }
    private Callback isAliveCallback = new Callback(){
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
    private Callback predictionDayCallback = new Callback(){
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
        }
    };
    private Callback predictionHoursCallback = new Callback(){
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
    private Callback getDataCallback = new Callback(){
        @Override
        public void onResponse(Call call, Response response) throws IOException{
            try(ResponseBody responseBody = response.body()){
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                Log.i("ApiNetwork", "Code: " + response.code());

                //String jsonData = responseBody.string();
                //Log.i("ApiNetwork", "json: " + jsonData);

                //Data json = gistJsonAdapter.fromJson(response.body().source());

                try {
                    JSONArray json = new JSONArray(responseBody.string());
                    Log.i("ApiNetwork", "json: " + json);
                    //JSONArray precipitaciones = json.getJSONArray("probPrecipitacion");
                    //Log.i("ApiNetwork", "Precip: " + precipitaciones.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }
    };
    public  void getData(String url){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json; q=0.5")
                .method("GET", null)
                .build();
        HttpClient.asyncRequest(HttpMethod.GET, request, getDataCallback);
    }
    public  void isAlive(){
        Request request = new Request.Builder()
                .url("http://publicobject.com/helloworld.txt")
                .method("GET", null)
                .build();
        HttpClient.asyncRequest(HttpMethod.GET, request, isAliveCallback);
    }
    public  JSONObject getSpainTowns(String location) throws IOException {
        Request request = new Request.Builder()
                .url(URL+Endpoints.MUNICIPIOS)
                .method("GET", null)
                .build();
        HttpClient.syncRequest(HttpMethod.GET, request);

        //Parse response un JSON
        return null;
    }
    public  void getTownPredictionHours(String id) throws IOException {
        Request request = new Request.Builder()
                .url(URL+Endpoints.MUNICIPIO_HORARIA+"/"+id)
                .addHeader("API_KEY", API_KEY)
                .method("GET", null)
                .build();

        Log.i("ApiNetwork", "Request: " + request.toString());
        HttpClient.asyncRequest(HttpMethod.GET, request, predictionHoursCallback);

    }
    public  void getTownPredictionDay(String id) throws IOException {
        Request request = new Request.Builder()
                .url(URL+Endpoints.MUNICIPIO_DIARIA+id)
                .addHeader("API_KEY", API_KEY)
                .method("GET", null)
                .build();
        Log.i("ApiNetwork", "Request: " + request.toString());
        HttpClient.asyncRequest(HttpMethod.GET, request, predictionDayCallback);

    }

}
