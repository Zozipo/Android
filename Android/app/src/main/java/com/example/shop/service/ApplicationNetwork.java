package com.example.shop.service;

import com.example.shop.contants.Urls;
import com.example.shop.network.AccountApi;
import com.example.shop.network.CategoriesApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationNetwork {
    public static ApplicationNetwork mInstance;
    private static final String BASE_URL = Urls.BASE;
    private Retrofit mRetrofit;
    public ApplicationNetwork() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApplicationNetwork getInstance() {
        if (mInstance == null)
            mInstance = new ApplicationNetwork();
        return mInstance;
    }

    public CategoriesApi getCategoriesApi() {
        return mRetrofit.create(CategoriesApi.class);
    }

    public AccountApi getAccountApi() {
        return mRetrofit.create(AccountApi.class);
    }
}
