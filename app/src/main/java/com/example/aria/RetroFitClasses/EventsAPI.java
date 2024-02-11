package com.example.aria.RetroFitClasses;

import com.example.aria.ConvertToJSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public EventsAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void updateTitle(int id, String title) {
        ConvertToJSON s=new ConvertToJSON(title);
        Call<Void> call = webServiceAPI.updateTitle(id, s);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("onResponse");
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("fail");
                System.out.println(t);
                System.out.println(call);
            }
        });
    }
}
