package com.example.aria.RetroFitClasses;
import com.example.aria.AriaEventsItems;
import com.google.gson.JsonObject;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ChatsAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;
    JsonObject ja;
    List<List<AriaEventsItems>>  events;

    public ChatsAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.5:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
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
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                }
            });
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
        return ja;
    }

    public List<List<AriaEventsItems>> getAriaList(String username) {
        Call<List<List<AriaEventsItems>>> call = webServiceAPI.getAriaList(username);
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
}
