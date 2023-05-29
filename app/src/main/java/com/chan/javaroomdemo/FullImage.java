package com.chan.javaroomdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class FullImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        String image = getIntent().getExtras().getString("image");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ALPHA_8;
        Bitmap bitmap = BitmapFactory.decodeFile(image,options);
        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

        ImageView imageView  = findViewById(R.id.imageView2);
        imageView.setImageBitmap(bitmap);
    }
}