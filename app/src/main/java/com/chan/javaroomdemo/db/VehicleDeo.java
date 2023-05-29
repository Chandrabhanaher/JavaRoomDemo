package com.chan.javaroomdemo.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface VehicleDeo {

    @Query("SELECT * FROM vehicle")
    LiveData<List<Vehicle>> getAll();


    @Insert
    void insertVehicle(Vehicle vehicle);

}
