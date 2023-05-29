package com.chan.javaroomdemo.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SchoolDao {

    @Insert
    void insertAll(School... school);

    @Query("SELECT * FROM school")
    LiveData<List<School> > getAll();

    @Query("SELECT * FROM school")
    List<School> getAllSchools();
}
