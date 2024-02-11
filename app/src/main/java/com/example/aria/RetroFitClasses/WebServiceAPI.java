package com.example.aria.RetroFitClasses;
import com.example.aria.ConvertToJSON;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface WebServiceAPI {


        @POST("Users")
        Call<Void> addUser(@Body User user);

        @GET("Users/{id}")
        Call<List<Event>> getEvents(@Path("id") String username);

        @PUT("Events/{id}/Title")
        Call<Void> updateTitle(@Path("id") int id, @Body ConvertToJSON title);

        /*@GET("Users/{id}")
        Call<User> getUsers(@Path("id") String id, @Header("authorization") String token);

        @POST("Users")
        Call<Void> createPost(@Body UserPassName user);

        @POST("Tokens")
        Call<String> createPost(@Body UserPass user);

        @GET("Chats/{id}/Messages")
        Call<List<Message>> getMessages(@Path("id") int id, @Header("authorization") String token);

        @POST("Chats/{id}/Messages")
        Call<Message> createPost(@Path("id") int id, @Header("authorization") String token, @Body ConvertToJSON msg);

        @GET("Chats/{id}")
        Call<Chat> getChatsById(@Path("id") int id, @Header("authorization") String token);

        @GET("Chats")
        Call<List<UserChat>> getChats(@Header("authorization") String token);

        @POST("Chats")
        Call<NewChat> createChat(@Header("authorization") String token, @Body ConvertToJsonChat username);

        @POST("AppToken")
        Call<Void> createPost(@Body AppToken appToken);
        @DELETE("AppToken/{id}")
        Call<Void> deleteToken(@Path("id") String id);*/

/*
@DELETE("posts/{id}")
Call<Void> deletePost(@Path("id") int id);

 */
}
