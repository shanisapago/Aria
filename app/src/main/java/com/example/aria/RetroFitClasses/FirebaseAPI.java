package com.example.aria.RetroFitClasses;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class FirebaseAPI {
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public FirebaseAPI() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.5:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void sendMessage(String title, String description, String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", title);
        jsonObject.addProperty("description", description);
        jsonObject.addProperty("token", token);
        Call<Void> call = webServiceAPI.sendMessage(jsonObject);
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
}