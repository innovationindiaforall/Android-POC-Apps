package com.mvp.call.presenter;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;


import androidx.annotation.RequiresApi;

import com.mvp.call.database.ContactDetails;
import com.mvp.call.view.MainActivity;
import com.mvp.call.presenter.ContactsPresenter;
import java.util.ArrayList;


public class ContactsPresenter {

    private MainActivity mainActivityObject;
    private Cursor phones;
    private ArrayList<ContactDetails> selectedUsers;

    public ContactsPresenter(MainActivity activity) {
        this.mainActivityObject = activity;
        selectedUsers = new ArrayList<>();

    }

    public void initialize() {
        mainActivityObject.initializePageSetup();
        mainActivityObject.checkPermissions();
    }

    public void prepareData() {
        phones = mainActivityObject.getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        LoadContact loadContact = new LoadContact();
        loadContact.execute();
    }

    class LoadContact extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone
            if (phones != null) {
                Log.v("Call", "" + phones.getCount());
                if (phones.getCount() == 0) {

                }
                while (phones.moveToNext()) {
                    String id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    ContactDetails selectUser = new ContactDetails();
                    selectUser.setName(name);
                    selectUser.setPhone(phoneNumber);
                    selectedUsers.add(selectUser);
                }
            } else {
                Log.v("Call", "----------------");
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            int count = selectedUsers.size();
            mainActivityObject.removeDuplicateContacts(selectedUsers);
            mainActivityObject.showContactsRecyclerView(selectedUsers);
        }
    }
}
