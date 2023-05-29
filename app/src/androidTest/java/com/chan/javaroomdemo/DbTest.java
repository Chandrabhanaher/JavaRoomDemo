package com.chan.javaroomdemo;

import static org.hamcrest.Matchers.equalTo;

import android.content.Context;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.chan.javaroomdemo.db.AppDatabase;
import com.chan.javaroomdemo.db.School;
import com.chan.javaroomdemo.db.SchoolDao;

import junit.framework.TestCase;

import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class DbTest extends TestCase {


    private AppDatabase appDatabase;
    private SchoolDao schoolDao;



    @Before
    public void setupDB(){
        Context mContext = InstrumentationRegistry.getInstrumentation().getContext();
        //Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(mContext, AppDatabase.class).allowMainThreadQueries().build();

        schoolDao = appDatabase.schoolDao();
    }


    @After
    public void closeDb(){
        appDatabase.close();
    }

    @Test
    public void schoolTest(){
        School school = new School();
        school.setName("PK");
        school.setUniversity("PUNE");

        schoolDao.insertAll(school);

       List<School> allData  = schoolDao.getAllSchools();
       Assert.assertEquals(1, allData.size());
        Assert.assertEquals("PK", allData.get(0).name);

        MatcherAssert.assertThat("Data", allData.get(0).name, equalTo("PK"));

    }
}
