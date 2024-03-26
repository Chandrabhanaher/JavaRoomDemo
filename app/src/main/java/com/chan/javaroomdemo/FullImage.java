package com.chan.javaroomdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import java.util.Objects;

public class FullImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        String imagePath = getIntent().getExtras().getString("image");
        String videoPath = getIntent().getExtras().getString("video");

        Log.e("IMAGE_PATH", "Video Path : "+ videoPath+" Image Path : "+imagePath);

        VideoView videoView = findViewById(R.id.videoView);
        ImageView imageView  = findViewById(R.id.imageView2);

        if(!Objects.equals(imagePath, null)){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ALPHA_8;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);
            bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
        }
        if(!Objects.equals(videoPath, null)){
            videoView.setVideoPath(videoPath);
            videoView.start();
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
        }

        // Uri uri = Uri.parse("android.resource://"+this.getApplicationContext().getPackageName()+"logo.mp4");
        //Uri uri = Uri.parse(path);


        // videoView.setVideoURI(uri);








    }
}