package com.example.aria.RetroFitClasses;

import com.example.aria.Alert;
import com.example.aria.IdClass;
import com.example.aria.JoinEvent;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    String id;

    public EventsAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.5:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public String addEvent(String token, String title, String description, String start, String end, String alertString, String date) {
        NewEvent event = new NewEvent("0", token, title, description, start, end, alertString, date);
        Call<IdClass> call = webServiceAPI.addEvent(event);
        Thread t=new Thread((() -> {
            try{
                id=call.execute().body().getId();
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
        return id;
    }

    public void joinEvent(int id, String[] users, Alert[] alert) {
        JoinEvent e = new JoinEvent(users, alert, id);
        Call<Void> call = webServiceAPI.joinEvent(e);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    public void updateTitle(int id, String title) {
        //ConvertToJSON s=new ConvertToJSON(title);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", title);
        Call<Void> call = webServiceAPI.updateTitle(id, jsonObject);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    public void updateDescription(int id, String description) {
        //ConvertToJSONDes s=new ConvertToJSONDes(description);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("description", description);
        Call<Void> call = webServiceAPI.updateDescription(id, jsonObject);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    public void updateDate(int id, String date) {
        //ConvertToJSONDate s=new ConvertToJSONDate(date);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("date", date);
        Call<Void> call = webServiceAPI.updateDate(id, jsonObject);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    public void updateStart(int id, String start) {
        //ConvertToJSONStart s=new ConvertToJSONStart(start);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("start", start);
        Call<Void> call = webServiceAPI.updateStart(id, jsonObject);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    public void updateEnd(int id, String end) {
        //ConvertToJSONEnd s=new ConvertToJSONEnd(end);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("end", end);
        Call<Void> call = webServiceAPI.updateEnd(id, jsonObject);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    public void deleteByDate(String date) {
        Call<Void> call = webServiceAPI.deleteByDate(date);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    public void updateAlert(int id, String username,String alert) {
        //ConvertToJSON s=new ConvertToJSON();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("alert", alert);

        Call<Void> call = webServiceAPI.updateAlert(id, jsonObject);
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

    public void updateAll(int id, String token,String title,String start,String end,String date,String alert,String description) {
        //ConvertToJSON s=new ConvertToJSON();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", token);
        jsonObject.addProperty("title", title);
        jsonObject.addProperty("start", start);
        jsonObject.addProperty("end", end);
        jsonObject.addProperty("date", date);
        jsonObject.addProperty("alert", alert);
        jsonObject.addProperty("description", description);

        Call<Void> call = webServiceAPI.updateAll(id, jsonObject);
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

    public void deleteEventById(int id, String username) {
        //ConvertToJSON s=new ConvertToJSON();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);


        Call<Void> call = webServiceAPI.deleteEventById(id, username);
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
