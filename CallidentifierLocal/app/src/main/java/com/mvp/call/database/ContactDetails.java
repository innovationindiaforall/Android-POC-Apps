package com.mvp.call.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.mvp.call.database.ContactDetails;

@Entity(tableName = "MyCustomContacts_Table")
public class ContactDetails {

    @PrimaryKey(autoGenerate = true)
    int id;
    String name;
    String phone;
    String selectedMobile;

    public String getSelectedMobile() {
        return selectedMobile;
    }

    public void setSelectedMobile(String selectedMobile) {
        this.selectedMobile = selectedMobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
