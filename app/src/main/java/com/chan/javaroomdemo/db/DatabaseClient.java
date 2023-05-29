package com.chan.javaroomdemo.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Chandrabhan Haribhau Aher on 02-03-2023.
 * chandrabhan99@gmail.com
 */
public class DatabaseClient {


    private static volatile AppDatabase INSTANCE;


    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE IF NOT EXISTS `Vehicle` (`vid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "`vehicle_name` TEXT," +
                    "`km` INTEGER NOT NULL)");

        }
    };


    static AppDatabase getInstance(final Context context){
        if(INSTANCE ==  null){
            synchronized (AppDatabase.class){
                if(INSTANCE ==  null){
                    SupportFactory supportFactory = new SupportFactory(SQLiteDatabase.getBytes("CHAN".toCharArray()));
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "school_db")
                            .openHelperFactory(supportFactory)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
