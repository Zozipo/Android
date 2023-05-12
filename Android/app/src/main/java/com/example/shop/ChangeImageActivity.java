package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.oginotihiro.cropview.CropUtil;
import com.oginotihiro.cropview.CropView;

import java.io.File;

public class ChangeImageActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 20;
    private CropView cropView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);
        cropView=findViewById(R.id.cropView);

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
       if(requestCode == RESULT_LOAD_IMAGE) {
           if(resultCode==RESULT_OK && data!=null) {
               Uri selectImage = data.getData();
               cropView.of(selectImage).asSquare().initialize(this);
           }
           else {
               finish();
           }
       }
       else {
           finish();
       }
    }

    public void RotateRightImage(View view) {
        cropView.setRotation(cropView.getRotation()+90);
    }
    public void RotateLeftImage(View view) {
        cropView.setRotation(cropView.getRotation()-90);
    }

    public void ChangeImage(View view) {
        String fileTemp = java.util.UUID.randomUUID().toString();
        Bitmap croppedBitmap = cropView.getOutput();
        Matrix matrix = new Matrix();
        matrix.postRotate(cropView.getRotation());
        Bitmap rotatedBitmap = Bitmap.createBitmap(croppedBitmap, 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight(), matrix, true);

        Uri fileSavePath = Uri.fromFile(new File(getCacheDir(), fileTemp));
        CropUtil.saveOutput(this, fileSavePath, rotatedBitmap, 90);

        //Вертаємо результат - Шлях до файлу, який збережено через кропер
        Intent intent = new Intent();
        intent.putExtra("croppedUri", fileSavePath);
        setResult(300, intent);
        finish();
    }
}