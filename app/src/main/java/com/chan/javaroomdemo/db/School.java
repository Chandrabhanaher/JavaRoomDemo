package com.chan.javaroomdemo.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class School {

    @PrimaryKey(autoGenerate = true)
    public int sid;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "university")
    public String university;


    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}
