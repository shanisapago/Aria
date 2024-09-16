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

    List<String> lst;
    List<AriaTimes>ariaTimesList;
    private Boolean successful=false;

    public UsersAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.5:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }
    public Boolean checkUsername(String username) {
        final int CORRECT_CODE=200;


        Call<Void> call = webServiceAPI.checkUsername(username);
        Thread t = new Thread((() -> {
            try {
                if(call.execute().code()==CORRECT_CODE)
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
public void post(String username, String password, String phoneNumber, String token, String fullName) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("username", username);
    jsonObject.addProperty("password", password);
    jsonObject.addProperty("phoneNumber", phoneNumber);
    jsonObject.addProperty("token", token);
    jsonObject.addProperty("fullName", fullName);
    Call<Void> call = webServiceAPI.addUser(jsonObject);

    Thread t=new Thread((() -> {
        try{
            call.execute().body();
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


    public void updateToken(String id, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("token", token);
        Call<Void> call = webServiceAPI.updateToken(id, jsonObject);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
    public List<String> getTimes(int day, String token) {
        Call<List<String>> call = webServiceAPI.getTimes(day,token);
        Thread t=new Thread((() -> {
            try{
                lst=call.execute().body();
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
        return lst;
    }
    public List<AriaTimes> getTimesAriaSort(String token) {
        Call<List<AriaTimes>> call = webServiceAPI.getTimesAriaSort(token);
        Thread t=new Thread((() -> {
            try{
                ariaTimesList=call.execute().body();
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
        return ariaTimesList;
    }
    public Boolean calendarTimes(int day, String time, int flag, String token) {
        final int CORRECT_CODE=200;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("day", day);
        jsonObject.addProperty("time", time);
        jsonObject.addProperty("flag", flag);
        jsonObject.addProperty("token", token);
        Call<Void> call = webServiceAPI.calendarTimes(jsonObject);
        Thread t = new Thread((() -> {
            try {
                if(call.execute().code()==CORRECT_CODE)
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
    public Boolean ariaTimes(List<Integer> day, List<String> time, List<Integer> flag, String token) {
        final int CORRECT_CODE=200;
        UpdateAriaTimes updateAriaTimes = new UpdateAriaTimes(time,day,flag,token);
        Call<Void> call = webServiceAPI.ariaTimes(updateAriaTimes);
        Thread t = new Thread((() -> {
            try {
                if(call.execute().code()==CORRECT_CODE)
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
}
