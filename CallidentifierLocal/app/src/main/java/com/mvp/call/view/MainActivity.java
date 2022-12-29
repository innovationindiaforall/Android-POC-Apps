package com.mvp.call.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.mvp.call.R;
import com.mvp.call.adapter.ContactsRecyclerAdapter;
import com.mvp.call.database.ContactDetails;
import com.mvp.call.database.MyContactsDB;
import com.mvp.call.presenter.ContactsPresenter;
import com.mvp.call.service.CallBackgroundService;
import com.mvp.call.utils.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactsRecyclerAdapter contactsRecyclerAdapter;
    private Context mContext = MainActivity.this;
    private ContactsPresenter contactsPresenter;
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 1234;

    private MyContactsDB contactsDB;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactsPresenter = new ContactsPresenter(MainActivity.this);
        contactsPresenter.initialize();

        Log.v("Call", ".....................................MainActivity onCreate");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            callService();
        }

        initDB();

        //-----------------Internal Testing Purpose................
        //deleteDB();
        //saveDataTestPurposeDirectly();
        //retrieveData();
    }

    private void retrofit() {

    }

    private void deleteDB() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                contactsDB.userDao().deleteAllContacts();
            }
        });

    }

    private void saveDataTestPurposeDirectly() {
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setName("Krish");
        contactDetails.setPhone("9994300000");
        contactDetails.setSelectedMobile("9994300000");

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                contactsDB.userDao().insert(contactDetails);
            }
        });
    }

    private void retrieveData() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<ContactDetails> contactDetails = MyContactsDB.getInstance(mContext).userDao().getAllUsersContacts();
                Log.v("Call", "......from DB...............contactDetails:" + contactDetails.size());
                for (int i = 0; i < contactDetails.size(); i++) {
                    String name = contactDetails.get(i).getName();
                    String mobile = contactDetails.get(i).getPhone();
                    String savedPosition = contactDetails.get(i).getSelectedMobile();
                    Log.v("Call", "......from DB...............name:" + name);
                    Log.v("Call", "......from DB..............mobile:" + mobile);
                    Log.v("Call", "......from DB..............savedPosition:" + savedPosition);
                }
            }
        });

    }


    //* method to init room database*//*
    private void initDB() {
        //MyContactsDB.getInstance(mContext);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        contactsDB = Room.databaseBuilder(getApplicationContext(),
                                MyContactsDB.class, "MyCustomContacts_Table")
                                .fallbackToDestructiveMigration()
                                .build();
                    }
                });
            }
        });

    }

    private void callService() {
        Log.v("Call", "callService()...............................MyBgService.class starting!!!");
        Intent backgroundService = new Intent(MainActivity.this, CallBackgroundService.class);
        startService(backgroundService);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("Call", ".......................................MainActivity onDestroy");
    }

    //----------------------------------------------------------------------------------------------
    public void checkPermissions() {

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.MODIFY_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_PHONE_STATE, Manifest.permission.MODIFY_PHONE_STATE
                    },
                    Constant.ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
            Log.v("Call", "if....requestPermissions()................................................");
        } else {
            Log.v("Call", "else....requestPermissions()................................................");
            //Toast.makeText(mContext, "until you get the permission,will not allow contacts", Toast.LENGTH_LONG).show();
            contactsPresenter.prepareData();
        }
    }

    //----------------------------------------------------------------------------------------------
    public void initializePageSetup() {

        recyclerView = (RecyclerView) findViewById(R.id.contacts_list);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        recyclerView.setLayoutManager(linearLayoutManager); // set LayoutManager to RecyclerView

    }

    //----------------------------------------------------------------------------------------------
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == Constant.ASK_MULTIPLE_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                contactsPresenter.prepareData();
            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == Constant.ASK_MULTIPLE_PERMISSION_REQUEST_CODE) {
            contactsPresenter.prepareData();
        } else {
            Toast.makeText(this, "Until you grant the permission of phone no", Toast.LENGTH_SHORT).show();
        }
    }

    //..............................................................................................
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            Toast.makeText(this, "onActivityResult()..overlay granted........", Toast.LENGTH_SHORT).show();
            callService();
        } else {
            Toast.makeText(this, "onActivityResult()..overlay NOT granted........", Toast.LENGTH_SHORT).show();
        }
    }

    //..............................................................................................
    public void removeDuplicateContacts(ArrayList<ContactDetails> selectedUsers) {

        ArrayList<ContactDetails> removed = new ArrayList<>();
        ArrayList<ContactDetails> contacts = new ArrayList<>();
        for (int i = 0; i < selectedUsers.size(); i++) {
            ContactDetails inviteFriendsProjo = selectedUsers.get(i);
            if (inviteFriendsProjo.getName().matches("\\d+(?:\\.\\d+)?") || inviteFriendsProjo.getName().trim().length() == 0) {
                removed.add(inviteFriendsProjo);
            } else {
                contacts.add(inviteFriendsProjo);
            }
        }
        contacts.addAll(removed);
        for (int i = 0; i < contacts.size(); i++) {

            for (int j = i + 1; j < contacts.size(); j++) {
                if (contacts.get(i).getName().trim().equals(contacts.get(j).getName().trim())) {
                    contacts.remove(j);
                    j--;
                }
            }

        }
        selectedUsers.clear();
        for (int i = 0; i < contacts.size(); i++) {
            contacts.get(i).getName();
            contacts.get(i).getPhone();

            ContactDetails contactDetails = new ContactDetails();
            contactDetails.setName(contacts.get(i).getName());
            contactDetails.setPhone(contacts.get(i).getPhone());
            selectedUsers.add(contactDetails);
        }

        insertData(contacts);
    }

    private void insertData(ArrayList<ContactDetails> contacts) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < contacts.size(); i++) {
                    ContactDetails contactDetails = new ContactDetails();
                    contactDetails.setName(contacts.get(i).getName());
                    contactDetails.setPhone(contacts.get(i).getPhone());
                    contactDetails.setSelectedMobile("0");
                    MyContactsDB.getInstance(mContext).userDao().insert(contactDetails);
                }
            }
        });
    }

    //..............................................................................................
    public void showContactsRecyclerView(ArrayList<ContactDetails> selectedUsers) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contactsRecyclerAdapter = new ContactsRecyclerAdapter(inflater, mContext, selectedUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(contactsRecyclerAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
    }
    //..............................................................................................

}