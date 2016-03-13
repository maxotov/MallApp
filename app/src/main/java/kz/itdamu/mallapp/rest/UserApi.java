package kz.itdamu.mallapp.rest;

import kz.itdamu.mallapp.entity.Message;
import kz.itdamu.mallapp.entity.User;
import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by Aibol on 13.03.2016.
 */
public interface UserApi {

    @Multipart
    @POST("/registerClient")
    void registerBasicUser(@Part("device_id") String device_id, Callback<Message> message);

    @Multipart
    @POST("/register")
    void register(@Part("name") String name, @Part("phone") String phone, @Part("email") String email, @Part("password") String password, @Part("device_id") String device_id, Callback<Message> message);

    @Multipart
    @POST("/login")
    void login(@Part("email") String email, @Part("password") String password, Callback<User> user);
}
