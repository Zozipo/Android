package com.example.shop;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shop.category.CategoriesAdapter;
import com.example.shop.category.CategoryEditActivity;
import com.example.shop.contants.Urls;
import com.example.shop.dto.category.CategoryItemDTO;
import com.example.shop.service.ApplicationNetwork;
import com.example.shop.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    CategoriesAdapter categoriesAdapter;
    RecyclerView rcvCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView iv = findViewById(R.id.imageView);
        String url = Urls.BASE+"/images/1.jpg";
        Glide.with(this)
                .load(url)
                .apply(new RequestOptions().override(600))
                .into(iv);
        rcvCategories = findViewById(R.id.rcvCategories);
        rcvCategories.setHasFixedSize(true);
        rcvCategories.setLayoutManager(new GridLayoutManager(this, 2,
                LinearLayoutManager.VERTICAL, false));
        rcvCategories.setAdapter(new CategoriesAdapter(new ArrayList<>(), MainActivity.this::onClickEditCategory));
        requestServer();
    }

    private void requestServer() {
        CommonUtils.showLoading();
        ApplicationNetwork
                .getInstance()
                .getCategoriesApi()
                .list()
                .enqueue(new Callback<List<CategoryItemDTO>>() {
                    @Override
                    public void onResponse(Call<List<CategoryItemDTO>> call, Response<List<CategoryItemDTO>> response) {
                        List<CategoryItemDTO> data = response.body();
                        if(response.isSuccessful() && data!=null) {
                            categoriesAdapter = new CategoriesAdapter(data, MainActivity.this::onClickEditCategory);
                            rcvCategories.setAdapter(categoriesAdapter);
                        }
                        CommonUtils.hideLoading();
                        //CategoryItemDTO cat = data.get(0);
                    }

                    @Override
                    public void onFailure(Call<List<CategoryItemDTO>> call, Throwable t) {
                        CommonUtils.hideLoading();
                    }
                });
    }

    private void onClickEditCategory(CategoryItemDTO category) {
        Intent intent = new Intent(MainActivity.this, CategoryEditActivity.class);
        Bundle b = new Bundle();
        b.putInt("id", category.getId());
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
        //Toast.makeText(this, "Редагуємо категорію "+category.getId(), Toast.LENGTH_SHORT).show();
    }
}