package com.example.shop.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.example.shop.BaseActivity;
import com.example.shop.ChangeImageActivity;
import com.example.shop.MainActivity;
import com.example.shop.R;
import com.example.shop.category.CategoryCreateActivity;
import com.example.shop.dto.account.RegisterDTO;
import com.example.shop.service.ApplicationNetwork;
import com.example.shop.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    int SELECT_CROPPER = 300;
    Uri uri = null;
    ImageView IVPreviewImage;

    TextInputEditText txtLastName;
    TextInputEditText txtFirstName;
    TextInputEditText txtEmail;
    TextInputEditText txtPassword;
    TextInputEditText txtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        IVPreviewImage = findViewById(R.id.IVPreviewImage);

        txtLastName = findViewById(R.id.txtLastName);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
    }

    public void handleRegisterClick(View view) {
        RegisterDTO dto = new RegisterDTO();
        dto.setFirstName(txtFirstName.getText().toString());
        dto.setLastName(txtLastName.getText().toString());
        dto.setEmail(txtEmail.getText().toString());
        dto.setPassword(txtPassword.getText().toString());
        dto.setConfirmPassword(txtConfirmPassword.getText().toString());
        dto.setPhoto(uriGetBase64(uri));
        CommonUtils.showLoading();
        ApplicationNetwork
                .getInstance()
                .getAccountApi()
                .register(dto)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()) {
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            ResponseBody error = response.errorBody();
                        }
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        CommonUtils.hideLoading();
                    }
                });


    }
    public void handleSelectImageClick(View view) {
        Intent intent = new Intent(this, ChangeImageActivity.class);
        startActivityForResult(intent, SELECT_CROPPER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==SELECT_CROPPER) {
            uri = (Uri) data.getParcelableExtra("croppedUri");
            IVPreviewImage.setImageURI(uri);
        }
    }

    private String uriGetBase64(Uri uri) {
        try {
            Bitmap bitmap=null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch(IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            byte[] byteArr = bytes.toByteArray();
            return Base64.encodeToString(byteArr, Base64.DEFAULT);

        } catch(Exception ex) {
            return null;
        }
    }
}