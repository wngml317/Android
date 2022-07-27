package com.wngml.memoapp.api;

import com.wngml.memoapp.model.UserRes;
import com.wngml.memoapp.model.User;

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
