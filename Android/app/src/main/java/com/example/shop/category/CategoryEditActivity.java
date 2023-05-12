package com.example.shop.category;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.shop.BaseActivity;
import com.example.shop.ChangeImageActivity;
import com.example.shop.MainActivity;
import com.example.shop.R;
import com.example.shop.application.HomeApplication;
import com.example.shop.contants.Urls;
import com.example.shop.dto.category.CategoryCreateDTO;
import com.example.shop.dto.category.CategoryItemDTO;
import com.example.shop.dto.category.CategoryUpdateDTO;
import com.example.shop.service.ApplicationNetwork;
import com.example.shop.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryEditActivity extends BaseActivity {

    int SELECT_CROPPER = 300;
    Uri uri = null;
    ImageView IVPreviewImage;
    int id=0;

    TextInputEditText txtCategoryNameEdit;
    TextInputEditText txtCategoryPriorityEdit;
    TextInputEditText txtCategoryDescriptionEdit;

    private TextInputLayout txtFieldCategoryNameEdit;
    private TextInputLayout txtFieldCategoryPriorityEdit;
    private TextInputLayout txtFieldCategoryDescriptionEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_edit);
        IVPreviewImage = findViewById(R.id.IVPreviewImage);

        txtCategoryNameEdit = findViewById(R.id.txtCategoryNameEdit);
        txtCategoryPriorityEdit = findViewById(R.id.txtCategoryPriorityEdit);
        txtCategoryDescriptionEdit = findViewById(R.id.txtCategoryDescriptionEdit);

        txtFieldCategoryNameEdit = findViewById(R.id.txtFieldCategoryNameEdit);
        txtFieldCategoryPriorityEdit = findViewById(R.id.txtFieldCategoryPriorityEdit);
        txtFieldCategoryDescriptionEdit = findViewById(R.id.txtFieldCategoryDescriptionEdit);

        Bundle b = getIntent().getExtras();
        int value = 0; // or other values
        if(b != null)
            value = b.getInt("id");
        id=value;
        //Toast.makeText(this, "ID Edit "+ value, Toast.LENGTH_SHORT).show();
        serverRequestById();
        setupError();
    }
    private void serverRequestById() {

        ApplicationNetwork.getInstance()
                .getCategoriesApi()
                .getById(id)
                .enqueue(new Callback<CategoryItemDTO>() {
                    @Override
                    public void onResponse(Call<CategoryItemDTO> call, Response<CategoryItemDTO> response) {
                        //Toast.makeText(CategoryEditActivity.this, "", Toast.LENGTH_SHORT).show();
                        CategoryItemDTO dto=response.body();
                        txtCategoryNameEdit.setText(dto.getName());
                        txtCategoryPriorityEdit.setText(Integer.toString(dto.getPriority()));
                        txtCategoryDescriptionEdit.setText(dto.getDescription());
                        String url= Urls.BASE+dto.getImage();
                        Glide.with(HomeApplication.getAppContext())
                                .load(url)
                                .apply(new RequestOptions().override(600))
                                .into(IVPreviewImage);
                    }

                    @Override
                    public void onFailure(Call<CategoryItemDTO> call, Throwable t) {

                    }
                });
    }

    private void setupError() {

        txtFieldCategoryNameEdit.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() >= 0 && text.length() <= 2) {
                    txtFieldCategoryNameEdit.setError(getString(R.string.category_name_required));
                    txtFieldCategoryNameEdit.setErrorEnabled(true);
                } else {
                    txtFieldCategoryNameEdit.setErrorEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtFieldCategoryPriorityEdit.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                int number=0;
                try {
                    number = Integer.parseInt(text.toString());
                }
                catch (Exception ex) {

                }
                if (number<=0) {
                    txtFieldCategoryPriorityEdit.setError(getString(R.string.category_priority_required));
                    txtFieldCategoryPriorityEdit.setErrorEnabled(true);
                } else {
                    txtFieldCategoryPriorityEdit.setErrorEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtFieldCategoryDescriptionEdit.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() >= 0 && text.length() <= 2) {
                    txtFieldCategoryDescriptionEdit.setError(getString(R.string.category_description_required));
                    txtFieldCategoryDescriptionEdit.setErrorEnabled(true);
                } else {
                    txtFieldCategoryDescriptionEdit.setErrorEnabled(false);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void handleCreateCategoryClick(View view) {
        if(!validation()) {
            Toast.makeText(this, "Заповніть усі поля!", Toast.LENGTH_LONG).show();
            return;
        }
        CategoryUpdateDTO model = new CategoryUpdateDTO();
        model.setId(id);
        model.setName(txtCategoryNameEdit.getText().toString());
        int number = Integer.parseInt(txtCategoryPriorityEdit.getText().toString());
        model.setPriority(number);
        model.setDescription(txtCategoryDescriptionEdit.getText().toString());
        model.setImageBase64(uriGetBase64(uri));
        CommonUtils.showLoading();
        ApplicationNetwork
                .getInstance()
                .getCategoriesApi()
                .update(model)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Intent intent = new Intent(CategoryEditActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        CommonUtils.hideLoading();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        CommonUtils.hideLoading();
                    }
                });
    }

    private boolean validation() {
        boolean isValid=true;
        String name = txtCategoryNameEdit.getText().toString();
        if(name.isEmpty() || name.length()<=2) {
            txtFieldCategoryNameEdit.setError(getString(R.string.category_name_required));
            isValid=false;
        }
        String priority = txtCategoryPriorityEdit.getText().toString();
        int number=0;
        try {
            number = Integer.parseInt(priority.toString());
        }
        catch (Exception ex) {

        }
        if (number<=0) {
            txtFieldCategoryPriorityEdit.setError(getString(R.string.category_priority_required));
            txtFieldCategoryPriorityEdit.setErrorEnabled(true);
            isValid=false;
        }
        String description = txtCategoryDescriptionEdit.getText().toString();
        if(description.isEmpty() || description.length()<=2) {
            txtFieldCategoryDescriptionEdit.setError(getString(R.string.category_description_required));
            txtFieldCategoryDescriptionEdit.setErrorEnabled(true);
            isValid=false;
        }

        return isValid;
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
}