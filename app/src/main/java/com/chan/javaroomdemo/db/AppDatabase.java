package com.chan.javaroomdemo.db;

import android.content.Context;


import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {School.class,Vehicle.class}, version = 2 ,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SchoolDao schoolDao();
    public abstract VehicleDeo vehicleDeo();
}
