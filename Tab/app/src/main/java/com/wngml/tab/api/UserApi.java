package com.wngml.tab.api;

import com.wngml.tab.model.User;
import com.wngml.tab.model.UserRes;

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
