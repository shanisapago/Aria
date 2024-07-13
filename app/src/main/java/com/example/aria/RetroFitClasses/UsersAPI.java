package com.example.aria.RetroFitClasses;
import com.google.gson.JsonObject;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersAPI {

    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    List<NewEvent> events;
    PhoneUsers pu;
    private Boolean successful=false;

    public UsersAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }
    public Boolean checkUsername(String username) {


        Call<Void> call = webServiceAPI.checkUsername(username);
        Thread t = new Thread((() -> {
            try {
                if(call.execute().code()==200)
                    successful=true;
            } catch (Exception e) {
            }
        }));
        t.start();
        try {
            t.join();
        } catch (Exception e) {
        }
        return successful;
    }

    public void post(String username, String password, String phoneNumber, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("phoneNumber", phoneNumber);
        jsonObject.addProperty("token", token);
        System.out.println("inUserAPI");
        System.out.println(token);
        Call<Void> call = webServiceAPI.addUser(jsonObject);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("in response");

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t);
                System.out.println("in on fail");
            }
        });
    }

    public List<NewEvent> getEvents(String username) {
        Call<List<NewEvent>> call = webServiceAPI.getEvents(username);
        Thread t=new Thread((() -> {
            try{
                events=call.execute().body();
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
        return events;
    }

    public PhoneUsers checkUser(PhoneArray phones) {
        Call<PhoneUsers> call = webServiceAPI.checkUser(phones);
        Thread t=new Thread((() -> {
            try{
                pu=call.execute().body();
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
        return pu;
    }
}