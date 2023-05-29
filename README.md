# Java Room Database with CameraX
```
Java Room Database
CameraX 
```

## CameraX dependancy
```
def camerax_version = "1.3.0-alpha02"
implementation "androidx.camera:camera-core:${camerax_version}"
implementation "androidx.camera:camera-camera2:${camerax_version}"
implementation "androidx.camera:camera-lifecycle:${camerax_version}"
implementation "androidx.camera:camera-video:${camerax_version}"

implementation "androidx.camera:camera-view:${camerax_version}"
implementation "androidx.camera:camera-extensions:${camerax_version}"
```

## AndroidManifest - permissons
```
<uses-feature android:name="android.hardware.camera.any" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="28" />
```
## Demo
```
  ListenableFuture<ProcessCameraProvider> cameraProviderFeature = ProcessCameraProvider.getInstance(this);
        cameraProviderFeature.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFeature.get();
                if (cameraProvider != null) {
                    startCameraX(cameraProvider);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
}, ContextCompat.getMainExecutor(this));

<!-- Start Camera X  -->
 cameraProvider.unbindAll();

 Preview preview = new Preview.Builder().build();
 preview.setSurfaceProvider(cameraPreviewView.getSurfaceProvider());
 CameraSelector cameraSelector = new CameraSelector.Builder()
          .requireLensFacing(CameraSelector.LENS_FACING_BACK)
          .build();
ImageCapture  imageCapture = new ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build();
ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture, imageAnalysis);
````
