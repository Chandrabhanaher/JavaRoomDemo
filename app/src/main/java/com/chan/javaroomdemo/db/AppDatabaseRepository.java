package com.chan.javaroomdemo.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AppDatabaseRepository {

    private SchoolDao schoolDao;
    private LiveData<List<School>>  allSchools;

    private VehicleDeo vehicleDeo;
    private LiveData<List<Vehicle>>  allVehicles;

    AppDatabaseRepository(Application application){
        AppDatabase appDatabase = DatabaseClient.getInstance(application);
        schoolDao = appDatabase.schoolDao();
        allSchools = schoolDao.getAll();

        vehicleDeo = appDatabase.vehicleDeo();
        allVehicles = vehicleDeo.getAll();
    }

    LiveData<List<School>> getAllSchools(){
        return allSchools;
    }

    void insertSchool(School school){
        DatabaseClient.databaseWriteExecutor.execute(()->{
            schoolDao.insertAll(school);
        });
    }

    LiveData<List<Vehicle>> getAllVehicles(){return allVehicles;}

    void insertVehicle(Vehicle vehicle){
        DatabaseClient.databaseWriteExecutor.execute(()->{
            vehicleDeo.insertVehicle(vehicle);
        });
    }

}

