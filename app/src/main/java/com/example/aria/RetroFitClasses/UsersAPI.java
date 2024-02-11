package com.example.aria.RetroFitClasses;

import com.example.aria.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Reader;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersAPI {

    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    User user;
    List<Event> events;

    public UsersAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void post(String username, String password, String phoneNumber) {
        user=new User(username, password, phoneNumber);
        Call<Void> call = webServiceAPI.addUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("onResponse");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("fail");
                 System.out.println(t);
                 System.out.println(call);
            }
        });
    }

    public List<Event> getEvents(String username) {
        Call<List<Event>> call = webServiceAPI.getEvents(username);
        Thread t=new Thread((() -> {
            try{
                events=call.execute().body();
            }
            catch (Exception e){
                System.out.println("exception");
                System.out.println(e);
            }
        }));
        t.start();
        try {
            t.join();
        }
        catch (Exception e){
        }
        return events;
    }
}
