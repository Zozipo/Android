package com.example.shop.network;

import com.example.shop.dto.account.LoginDTO;
import com.example.shop.dto.account.LoginResponse;
import com.example.shop.dto.account.RegisterDTO;
import com.example.shop.dto.category.CategoryCreateDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountApi {
    @POST("/api/account/register")
    public Call<Void> register(@Body RegisterDTO registerDTO);
    @POST("/api/account/login")
    public Call<LoginResponse> login(@Body LoginDTO loginDTO);
}
