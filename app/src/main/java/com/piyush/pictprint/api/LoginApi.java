package com.piyush.pictprint.api;

import com.piyush.pictprint.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginApi {

    @POST("login")
    public Call<LoginResponse> login(@Query("credentials")String credentials);

    @POST("signup")
    public Call<LoginResponse> signUp(@Query("credentials")String credentials);

}
