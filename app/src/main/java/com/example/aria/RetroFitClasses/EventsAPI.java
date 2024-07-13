package com.example.aria.RetroFitClasses;
import com.example.aria.Alert;
import com.example.aria.JoinEvent;
import com.example.aria.MemberListItem;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventsAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    String flag;
    String id;
    String res;
    List<PhoneUsers2> pu;
    List<MembersNotificationsMsg> msg;
    NewEvent2 events;
    NewEvent ariaEvent;

    public EventsAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public NewEvent2 joinEvent(int id, String[] users, Alert[] alert) {
        JoinEvent e1 = new JoinEvent(users, alert, id);
        Call<NewEvent2> call = webServiceAPI.joinEvent(e1);
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
        Thread t=new Thread((() -> {
            try{
                call.execute();
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
    public NewEvent updateAriaResult(int id, String start,String date,String token) {
        //ConvertToJSON s=new ConvertToJSON();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("start", start);

        jsonObject.addProperty("date", date);
        jsonObject.addProperty("token", token);

        Call<NewEvent> call = webServiceAPI.updateAriaResult(id, jsonObject);
        Thread t=new Thread((() -> {
            try{
                ariaEvent = call.execute().body();
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
        return ariaEvent;
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                System.out.println("onResponse");
//                System.out.println(response);
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                System.out.println("fail");
//                System.out.println(t);
//                System.out.println(call);
//            }
//        });
    }
    public void deleteEventById(int id, String username) {
        //ConvertToJSON s=new ConvertToJSON();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);


        Call<Void> call = webServiceAPI.deleteEventById(id, username);
        Thread t=new Thread((() -> {
            try{
                call.execute();
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

    public void addGoogleEvent(int idEvent, int idGoogleEvent, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idEvent", idEvent);
        jsonObject.addProperty("idGoogleEvent", idGoogleEvent);
        jsonObject.addProperty("token", token);
        Call<Void> call = webServiceAPI.addGoogleEvent(jsonObject);
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

    public String idGoogle(int idEvent, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idEvent", idEvent);
        jsonObject.addProperty("token", token);
        Call<String> call = webServiceAPI.idGoogle(jsonObject);
        Thread t=new Thread((() -> {
            try{
                res = call.execute().body();
                System.out.println("ininin");
                System.out.println(res);
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
        return res;
    }

    public void deleteGoogle(int id, String username) {
        //ConvertToJSON s=new ConvertToJSON();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);


        Call<Void> call = webServiceAPI.deleteGoogle(id, username);
        Thread t=new Thread((() -> {
            try{
                call.execute();
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

    public String addEvent(String token, String title, String description, String start, String end, String alertString, String date, String flag) {
        NewAddEvent event = new NewAddEvent("0", token, title, description, start, end, alertString, date, flag);
        Call<String> call = webServiceAPI.addEvent(event);
        Thread t=new Thread((() -> {
            try{
                id=call.execute().body();
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
    public List<PhoneUsers2> checkPhones(String token, String title, String description, String start, String end, String alertString, String date, List<MemberListItem> phones, String tok, String flag) {
        System.out.println("in checks phone");
        System.out.println(tok);
        System.out.println(flag);
        NewEvent event_details=new NewEvent("0", token, title, description, start, end, alertString, date, phones, tok, flag);

        Call<List<PhoneUsers2>> call = webServiceAPI.checkPhones(event_details);
        Thread t=new Thread((() -> {
            try{
                pu=call.execute().body();
                System.out.println("pu");
                System.out.println(pu);
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
        return pu;
    }


    public void deleteInvitation(int id, String username) {

        Call<Void> call = webServiceAPI.deleteInvitation(id, username);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
    public List<MembersNotificationsMsg> getMembersNotificationMsg(String username) {
        Call<List<MembersNotificationsMsg>> call = webServiceAPI.getMembersNotifications(username);
        Thread t=new Thread((() -> {
            try{
                msg=call.execute().body();
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
        return msg;
    }
}
