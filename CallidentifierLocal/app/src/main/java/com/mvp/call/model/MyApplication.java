package com.mvp.call.model;

import androidx.multidex.MultiDexApplication;
import androidx.room.Room;

import com.mvp.call.database.MyContactsDB;


public class MyApplication extends MultiDexApplication {

    private static MyContactsDB contactsDB;

    @Override
    public void onCreate() {
        super.onCreate();
        initDB();
    }

    /**
     * initialising the database
     */
    private void initDB() {
        contactsDB = Room.databaseBuilder(getApplicationContext(),
                MyContactsDB.class, "MyCustomContacts_Database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static MyContactsDB getMyDatabase() {
        return contactsDB;
    }


}
