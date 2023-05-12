package com.example.shop.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import com.example.shop.dto.category.CategoryCreateDTO;
import com.example.shop.dto.category.CategoryItemDTO;
import com.example.shop.dto.category.CategoryUpdateDTO;

import java.util.List;

public interface CategoriesApi {
    @GET("/api/categories/list")
    public Call<List<CategoryItemDTO>> list();

    @GET("/api/categories/{id}")
    public Call<CategoryItemDTO> getById(@Path("id") int id);
    @POST("/api/categories/create")
    public Call<Void> create(@Body CategoryCreateDTO categoryCreateDTO);

    @PUT("/api/categories/update")
    public Call<Void> update(@Body CategoryUpdateDTO category);
}
