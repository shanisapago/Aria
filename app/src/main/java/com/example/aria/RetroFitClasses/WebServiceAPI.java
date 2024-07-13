package com.example.aria.RetroFitClasses;
import com.example.aria.AriaEventsItems;
import com.example.aria.JoinEvent;
import com.google.gson.JsonObject;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {


        @POST("Users")
        Call<Void> addUser(@Body JsonObject user);
        @GET("Users/CheckUsername")
        Call<Void> checkUsername(@Header("username") String username);

        @PUT("Users/Phones")
        Call<PhoneUsers> checkUser(@Body PhoneArray phones);

        @POST("Tokens")
        Call<String> createPost(@Body JsonObject user);

        @GET("Users")
        Call<List<NewEvent>> getEvents(@Header("authorization") String token);

        @POST("Events")
        Call<String> addEvent(@Body NewAddEvent event);

        @POST("Events/Join")
        Call<NewEvent2> joinEvent(@Body JoinEvent event);

        @PUT("Events/{id}/Title")
        Call<Void> updateTitle(@Path("id") int id, @Body JsonObject title);

        @PUT("Events/{id}/Description")
        Call<Void> updateDescription(@Path("id") int id, @Body JsonObject description);

        @PUT("Events/{id}/Date")
        Call<Void> updateDate(@Path("id") int id, @Body JsonObject date);

        @PUT("Events/{id}/Start")
        Call<Void> updateStart(@Path("id") int id, @Body JsonObject start);

        @PUT("Events/{id}/End")
        Call<Void> updateEnd(@Path("id") int id, @Body JsonObject end);

        @DELETE("Events/{id}/Date")
        Call<Void> deleteByDate(@Path("id") String date);

        @PUT("Events/{id}/Alert")
        Call<Void> updateAlert(@Path("id") int id, @Body JsonObject Alert);

        @PUT("Events/{id}/All")
        Call<Void> updateAll(@Path("id") int id, @Body JsonObject All);
        @PUT("Events/{id}/AriaResult")
        Call<NewEvent> updateAriaResult(@Path("id") int id, @Body JsonObject AriaResult);

        @DELETE("Events/{id}/{username}/DeleteById")
        Call<Void> deleteEventById(@Path("id") int id, @Path("username") String username);
        @POST("Chats/AddChat")
        Call<Void> addChat(@Body JsonObject jsonObject);

        @POST("Chats/AddMessage")
        Call<JsonObject> addMessage(@Body JsonObject jsonobject);

        @DELETE("Chats/{id}/{token}")
        Call<Void> deleteChat(@Path("id") int id, @Path("token") String token);

        @POST("Events/AddGoogleEvent")
        Call<Void> addGoogleEvent(@Body JsonObject idEvent);

        @PUT("Events/GoogleEvent")
        Call<String> idGoogle(@Body JsonObject idEvent);

        @DELETE("Events/GoogleEvent/{id}/{username}")
        Call<Void> deleteGoogle(@Path("id") int id, @Path("username") String username);

        @DELETE("Events/{id}/{username}/DeleteInvitation")
        Call<Void> deleteInvitation(@Path("id") int id, @Path("username") String username);
        @GET("Events/getMembersNotifications")
        Call<List<MembersNotificationsMsg>> getMembersNotifications(@Header("authorization") String token);
        @POST("Events/checkPhones")
        Call<List<PhoneUsers2>> checkPhones(@Body NewEvent event);

        @POST("Firebase")
        Call<Void> sendMessage(@Body JsonObject jsonObject);

        @GET("Chats/GetAriaList")
        Call<List<List<AriaEventsItems>>> getAriaList(@Header("authorization") String token);
}
