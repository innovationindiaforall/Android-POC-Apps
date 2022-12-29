package com.mvp.call.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.mvp.call.database.MyContactsDB;
import com.mvp.call.database.*;

@Database(entities = {ContactDetails.class}, version = 1)
public abstract class MyContactsDB extends RoomDatabase {

    public abstract UserDao userDao();

    private static MyContactsDB myContactsDBInstance;

    public static synchronized MyContactsDB getInstance(Context context) {

        if (myContactsDBInstance == null) {
    // DB Initialisation: To initialize the Room Database we have to add the following code to our class
            myContactsDBInstance = Room.databaseBuilder(context.getApplicationContext(),
                    MyContactsDB.class, "MyCustomContacts_Table")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return myContactsDBInstance;
    }

    // below line is to create a callback for our room database.
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // this method is called when database is created
            // and below line is to populate our data.
            new PopulateDbAsyncTask(myContactsDBInstance).execute();
        }
    };

    // we are creating an async task class to perform task in background.
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(MyContactsDB instance) {
            UserDao dao = instance.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
