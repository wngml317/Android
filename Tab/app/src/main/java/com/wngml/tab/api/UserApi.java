package com.wngml.postingapp.api;

import com.wngml.postingapp.model.User;
import com.wngml.postingapp.model.UserRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserApi {

    @POST("/users/register")
    Call<UserRes> register(@Body User user);

    @POST("/users/login")
    Call<UserRes> login(@Body User user);

    @POST("/users/logout")
    Call<UserRes> logout(@Header("Authorization") String token);

}
