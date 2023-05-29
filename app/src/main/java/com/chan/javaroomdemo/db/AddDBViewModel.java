package com.chan.javaroomdemo.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AddDBViewModel extends AndroidViewModel {

    private AppDatabaseRepository databaseRepository;

    private final LiveData<List<School>> mAllSchool;

    private final LiveData<List<Vehicle>> mAllVehicle;


    public AddDBViewModel(@NonNull Application application) {
        super(application);

        databaseRepository = new AppDatabaseRepository(application);
        mAllSchool = databaseRepository.getAllSchools();

        mAllVehicle = databaseRepository.getAllVehicles();

    }

    public LiveData<List<School>> getAllSchools(){
        return mAllSchool;
    }

    public void insertSchool(School school){databaseRepository.insertSchool(school);}

    public LiveData<List<Vehicle>> getAllVehicles(){return mAllVehicle;}
    public void insertVehicle(Vehicle vehicle){databaseRepository.insertVehicle(vehicle);}

}
