package com.example.shop.category;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop.BaseActivity;
import com.example.shop.ChangeImageActivity;
import com.example.shop.MainActivity;
import com.example.shop.R;
import com.example.shop.dto.category.CategoryCreateDTO;
import com.example.shop.service.ApplicationNetwork;
import com.example.shop.utils.CommonUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryCreateActivity extends BaseActivity {

    int SELECT_CROPPER = 300;
    Uri uri = null;
    ImageView IVPreviewImage;
    TextView textImageError;

    TextInputEditText txtCategoryName;
    TextInputEditText txtCategoryPriority;
    TextInputEditText txtCategoryDescription;

    private TextInputLayout txtFieldCategoryName;
    private TextInputLayout txtFieldCategoryPriority;
    private TextInputLayout txtFieldCategoryDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_create);
        textImageError = findViewById(R.id.textImageError);

        IVPreviewImage = findViewById(R.id.IVPreviewImage);
        txtCategoryName = findViewById(R.id.txtCategoryName);
        txtCategoryPriority = findViewById(R.id.txtCategoryPriority);
        txtCategoryDescription = findViewById(R.id.txtCategoryDescription);

        txtFieldCategoryName = findViewById(R.id.txtFieldCategoryName);
        txtFieldCategoryPriority = findViewById(R.id.txtFieldCategoryPriority);
        txtFieldCategoryDescription = findViewById(R.id.txtFieldCategoryDescription);

        setupError();

    }

    private void setupError() {

        txtFieldCategoryName.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() >= 0 && text.length() <= 2) {
                    txtFieldCategoryName.setError(getString(R.string.category_name_required));
                    txtFieldCategoryName.setErrorEnabled(true);
                } else {
                    txtFieldCategoryName.setErrorEnabled(false);
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

        txtFieldCategoryPriority.getEditText().addTextChangedListener(new TextWatcher() {
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
                    txtFieldCategoryPriority.setError(getString(R.string.category_priority_required));
                    txtFieldCategoryPriority.setErrorEnabled(true);
                } else {
                    txtFieldCategoryPriority.setErrorEnabled(false);
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

        txtFieldCategoryDescription.getEditText().addTextChangedListener(new TextWatcher() {
            // ...
            @Override
            public void onTextChanged(CharSequence text, int start, int count, int after) {
                if (text.length() >= 0 && text.length() <= 2) {
                    txtFieldCategoryDescription.setError(getString(R.string.category_description_required));
                    txtFieldCategoryDescription.setErrorEnabled(true);
                } else {
                    txtFieldCategoryDescription.setErrorEnabled(false);
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
        CategoryCreateDTO model = new CategoryCreateDTO();
        model.setName(txtCategoryName.getText().toString());
        int number = Integer.parseInt(txtCategoryPriority.getText().toString());
        model.setPriority(number);
        model.setDescription(txtCategoryDescription.getText().toString());
        model.setImageBase64(uriGetBase64(uri));
        CommonUtils.showLoading();
        ApplicationNetwork
                .getInstance()
                .getCategoriesApi()
                .create(model)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Intent intent = new Intent(CategoryCreateActivity.this, MainActivity.class);
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
        String name = txtCategoryName.getText().toString();
        if(name.isEmpty() || name.length()<=2) {
            txtFieldCategoryName.setError(getString(R.string.category_name_required));
            isValid=false;
        }
        String priority = txtCategoryPriority.getText().toString();
        int number=0;
        try {
            number = Integer.parseInt(priority.toString());
        }
        catch (Exception ex) {

        }
        if (number<=0) {
            txtFieldCategoryPriority.setError(getString(R.string.category_priority_required));
            txtFieldCategoryPriority.setErrorEnabled(true);
            isValid=false;
        }
        String description = txtCategoryDescription.getText().toString();
        if(description.isEmpty() || description.length()<=2) {
            txtFieldCategoryDescription.setError(getString(R.string.category_description_required));
            txtFieldCategoryDescription.setErrorEnabled(true);
            isValid=false;
        }

        if(uri==null) {
            textImageError.setVisibility(View.VISIBLE);
            isValid=false;
        }
        else {
            textImageError.setVisibility(View.INVISIBLE);
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
            textImageError.setVisibility(View.INVISIBLE);
            IVPreviewImage.setImageURI(uri);
        }
    }



}