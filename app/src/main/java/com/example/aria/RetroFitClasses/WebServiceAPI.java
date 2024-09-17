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



        @POST("Tokens")
        Call<UserDetails> createPost(@Body JsonObject user);

        @GET("Users")
        Call<List<NewEvent>> getEvents(@Header("authorization") String token);

        @POST("Events")
        Call<String> addEvent(@Body NewAddEvent event);

        @POST("Events/Join")
        Call<NewEvent2> joinEvent(@Body JoinEvent event);

        @PUT("Events/{id}/All")
        Call<Void> updateAll(@Path("id") int id, @Body JsonObject All);
        @PUT("Events/{id}/AriaResult")
        Call<NewEvent> updateAriaResult(@Path("id") int id, @Body JsonObject AriaResult);

        @PUT("Events/{id}/{username}/DeleteById")
        Call<Title> deleteEventById(@Path("id") int id, @Path("username") String username);
        @POST("Chats/AddChat")
        Call<Void> addChat(@Body JsonObject jsonObject);

        @POST("Chats/AddMessage")
        Call<JsonObject> addMessage(@Body JsonObject jsonobject);



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
        @PUT("Users/{id}/UpdateToken")
        Call<Void> updateToken(@Path("id") String id, @Body JsonObject token);
        @GET("Users/{day}/GetTimes")
        Call <List<String>> getTimes(@Path("day") int day,@Header("authorization") String token);
        @GET("Users/GetTimesAriaSort")
        Call <List<AriaTimes>> getTimesAriaSort(@Header("authorization") String token);
        @PUT("Users/CalendarTimes")
        Call<Void> calendarTimes(@Body  JsonObject ct);
        @PUT("Users/AriaTimes")
        Call<Void> ariaTimes(@Body  UpdateAriaTimes ct);
}
