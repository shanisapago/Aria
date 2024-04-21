package com.example.aria.RetroFitClasses;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatsAPI {


    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    JsonObject ja;


    public ChatsAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.197:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }


    public JsonObject addMessage(String phone, String token, JsonObject message, String time) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phone",phone);
        jsonObject.addProperty("token",token);
        jsonObject.addProperty("message",message.toString());
        jsonObject.addProperty("time",time);


        Call<JsonObject> call = webServiceAPI.addMessage(jsonObject);
        Thread t=new Thread((() -> {
            try{
                ja=call.execute().body();
            }
            catch (Exception e){
            }
        }));
        t.start();
        try {
            t.join();
        }
        catch (Exception e){
        }
        System.out.println("in thread");
        System.out.println(ja);
        return ja;
    }


    public void addChat(int id, String phone, String time, String msg1, String msg2, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("phone", phone);
        jsonObject.addProperty("time", time);
        jsonObject.addProperty("msg1", msg1);
        jsonObject.addProperty("msg2", msg2);
        jsonObject.addProperty("token", token);
        Call<Void> call = webServiceAPI.addChat(jsonObject);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("response");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("failure");
                System.out.println(t.getMessage());
            }
        });
    }
    public void deleteChat(int id, String token) {
        //JsonObject jsonObject = new JsonObject();
        Call<Void> call = webServiceAPI.deleteChat(id, token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }



}
