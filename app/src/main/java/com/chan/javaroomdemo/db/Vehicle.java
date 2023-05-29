package com.chan.javaroomdemo.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Vehicle {

    @PrimaryKey(autoGenerate = true)
    private int vid;

    @ColumnInfo(name = "vehicle_name")
    private String vehicle_name;

    @ColumnInfo(name = "km")
    private int km;

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public int getKm() {
        return km;
    }


    public void setKm(int km) {
        this.km = km;
    }
}

