package com.chan.javaroomdemo;

import static androidx.camera.core.CameraX.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.VideoCapture;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HardwarePropertiesManager;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CameraView extends AppCompatActivity implements View.OnClickListener, ImageAnalysis.Analyzer {

    private ListenableFuture<ProcessCameraProvider> cameraProviderFeature;
    private Button btnTakePic, btnStartRecord;
    private PreviewView cameraPreviewView;
    private ImageCapture imageCapture;
    private VideoCapture videoCapture;
    private ImageAnalysis imageAnalysis;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_view);

        btnTakePic = findViewById(R.id.btnTakePic);
        btnStartRecord = findViewById(R.id.btnRecord);
        cameraPreviewView = findViewById(R.id.previewView);

        btnTakePic.setOnClickListener(this);
        btnStartRecord.setOnClickListener(this);

        cameraProviderFeature = ProcessCameraProvider.getInstance(this);
        cameraProviderFeature.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFeature.get();
                if (cameraProvider != null) {
                    startCameraX(cameraProvider);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }, getExecutor());
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(cameraPreviewView.getSurfaceProvider());

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();

        videoCapture = new VideoCapture.Builder().setVideoFrameRate(30)
                /*.setBitRate(480000)
                .setAudioBitRate(70000)
                .setAudioChannelCount(1)*/
                .build();

        imageAnalysis = new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture, videoCapture);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTakePic:
                capturePhoto();
                break;
            case R.id.btnRecord:
                if (btnStartRecord.getText() == getResources().getText(R.string.start_recording)) {
                    btnStartRecord.setText(getResources().getText(R.string.stop_recording));
                    recordVideo();
                } else {
                    btnStartRecord.setText(getResources().getText(R.string.start_recording));
                    videoCapture.stopRecording();
                }
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    private void recordVideo() {
        if (videoCapture != null) {

/*
            long timeStamp = System.currentTimeMillis();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");*/
            String videoFilePath = getFileVideoPath();

            File photoFile = new File(videoFilePath);
            if (!photoFile.exists()){
                photoFile.mkdirs();
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            videoCapture.startRecording(
                    new VideoCapture.OutputFileOptions.Builder(photoFile).build(), getExecutor(),
                    new VideoCapture.OnVideoSavedCallback() {
                        @Override
                        public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                            runOnUiThread(() -> {
                                String path = String.valueOf(outputFileResults.getSavedUri());
                                Log.e("IMAGE_PATH", photoFile.getPath() + "Path : "+path);
                                startActivity(new Intent(CameraView.this, FullImage.class)
                                        .putExtra("video", path));
                                Toast.makeText(CameraView.this, "Video has been saved successfully", Toast.LENGTH_LONG).show();
                            });
                        }

                        @Override
                        public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                            runOnUiThread(() -> {
                                Toast.makeText(CameraView.this, message, Toast.LENGTH_LONG).show();
                                Log.e("IMAGE_PATH", message);
                            });
                        }
                    }
            );
        }
    }

    private String getFileVideoPath() {

        Date date = new Date();
        String timeStamp = String.valueOf(date.getTime());
        String imageFileName = "VIDEO_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);

        try {
            storageDir = File.createTempFile(imageFileName, ".mp4", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(storageDir).getAbsolutePath();
    }

    private void capturePhoto() {

        String photoFilePath = getFilePath();
        File photoFile = new File(photoFilePath);//Environment.getExternalStorageDirectory() + "/" + UUID.randomUUID().toString() + ".jpg");

        if (!photoFile.exists())
            photoFile.mkdirs();

        Log.e("take_pic", photoFile.getPath());


        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();


        imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> {
                    Log.e("IMAGE_PATH", photoFile.getPath());
                    startActivity(new Intent(CameraView.this, FullImage.class).putExtra("image", photoFile.getPath()));
                    Toast.makeText(CameraView.this, "Photo has been saved successfully", Toast.LENGTH_LONG).show();
                });

            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(() -> {
                    Toast.makeText(CameraView.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("take_pic", exception.getMessage());
                });
            }
        });


    }

    private String getFilePath() {
        String timeStamp = String.valueOf(new Date().getTime());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            storageDir = File.createTempFile(imageFileName, ".jpg", storageDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return storageDir.getAbsolutePath();
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
        Log.e("IMAGE_PATH", "analyze : got the frame at " + image.getImageInfo().getTimestamp());
        image.close();
    }
}