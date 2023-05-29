package com.chan.javaroomdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chan.javaroomdemo.db.AddDBViewModel;
import com.chan.javaroomdemo.db.School;
import com.chan.javaroomdemo.db.Vehicle;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private AddDBViewModel schoolViewModel;

    private static int UPDATE_CODE = 22;
    AppUpdateManager appUpdateManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inAppUpdate();


        schoolViewModel = new ViewModelProvider(this).get(AddDBViewModel.class);

        schoolViewModel.getAllSchools().observe(this, data -> {
            for (School sc : data) {
                Log.e("MAIN_ACTIVITY", "School Name: " + sc.getName() + "\nUniversity Name: " + sc.getUniversity());
                ///Toast.makeText(this, "School Name: "+sc.getName()+"\nUniversity Name: "+sc.getUniversity(), Toast.LENGTH_LONG).show();
            }
        });


        EditText txtSchoolName = findViewById(R.id.editTextTextSchoolName);
        EditText txtUniName = findViewById(R.id.editTextTextUniName);
        Button btnSubmit = findViewById(R.id.button);
        Button btnCamera = findViewById(R.id.btnCamera);
        Button takePicture = findViewById(R.id.takePicture);
        Button KotlinTackPicture = findViewById(R.id.takeKotlinePicture);
        Button btnCamera2 = findViewById(R.id.btnCamera2);

        btnSubmit.setOnClickListener(v -> {
            String name = txtSchoolName.getText().toString();
            String uniName = txtUniName.getText().toString();

            if (!name.isEmpty()) {
                School school = new School();
                school.setName(name);
                school.setUniversity(uniName);
                schoolViewModel.insertSchool(school);
                txtSchoolName.setText(null);
                txtUniName.setText(null);
            }
        });

        Button btnPopup = findViewById(R.id.btnPopup);

        btnPopup.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this, ActivityPopupWindow.class));
        });
        KotlinTackPicture.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, KotlinCaptureImage.class));
        });


        btnCamera2.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Camera2API.class));
        });


        takePicture.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CapturePhoto.class));
        });

        btnCamera.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CameraView.class));
        });


        EditText txtVName = findViewById(R.id.txtVNam);
        EditText txtKilometer = findViewById(R.id.txtKilometer);
        Button bntVehicle = findViewById(R.id.btnVehicle);

        bntVehicle.setOnClickListener(v -> {
            String vName = txtVName.getText().toString();
            String vKilometer = txtKilometer.getText().toString();

            int km = Integer.parseInt(vKilometer);

            Vehicle vehicle = new Vehicle();
            vehicle.setVehicle_name(vName);
            vehicle.setKm(km);
            schoolViewModel.insertVehicle(vehicle);
            txtVName.setText(null);
            txtKilometer.setText(null);

            getVehicles();

        });


    }

    private void inAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);
        Task<AppUpdateInfo> task = appUpdateManager.getAppUpdateInfo();
        task.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.FLEXIBLE,
                            MainActivity.this,
                            UPDATE_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
        appUpdateManager.registerListener(listener);
    }

    InstallStateUpdatedListener listener = installState -> {
        if(installState.installStatus()  == InstallStatus.DOWNLOADED){
            popUp();
        }
    };

    private void popUp() {
        Snackbar snackbar  = Snackbar.make(findViewById(android.R.id.content), "App update almost done", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Reload", v -> appUpdateManager.completeUpdate());
        snackbar.setTextColor(Color.parseColor("#FF0000"));
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_CODE){
            if(resultCode  != RESULT_OK){
                Log.e("APP_UPDATE", "Update is something wrong ");
            }else{
                Log.e("APP_UPDATE", "Is Update successfully ");
            }
        }
    }

    private void getVehicles() {
        schoolViewModel.getAllVehicles().observe(this, data -> {
            for (Vehicle sc : data) {
                Log.e("MAIN_ACTIVITY", "Vehicle Name: " + sc.getVehicle_name() + "\nKilometer : " + sc.getKm());
                //Toast.makeText(this, "Vehicle Name: "+sc.getVehicle_name()+"\nKilometer : "+sc.getKm(), Toast.LENGTH_LONG).show();
            }

        });
    }
}