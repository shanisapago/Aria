package com.example.aria.RetroFitClasses;
import com.example.aria.UserPass;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
public class TokensAPI {

    private Retrofit retrofit;
    private WebServiceAPI webServiceAPI;
    private String token;
    public TokensAPI() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public String post(String username, String password) {
        UserPass user=new UserPass(username, password);

        Call<String> call = webServiceAPI.createPost(user);
        Thread t=new Thread((() -> {
            try{
                token=call.execute().body();
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
        return token;
    }
}

