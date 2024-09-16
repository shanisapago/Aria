package com.example.aria.RetroFitClasses;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class TokensAPI {

    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private UserDetails userDetails;
    public TokensAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.5:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public UserDetails post(String username, String password) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("password", password);

        Call<UserDetails> call = webServiceAPI.createPost(jsonObject);
        Thread t=new Thread((() -> {
            try{
                userDetails=call.execute().body();
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
        return userDetails;
    }
}

