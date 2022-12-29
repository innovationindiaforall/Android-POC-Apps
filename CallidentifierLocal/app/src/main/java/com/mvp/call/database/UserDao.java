package com.mvp.call.database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import com.mvp.call.database.UserDao;
// Creating a DAO Interface for our Database

@androidx.room.Dao
public interface UserDao {

    @Insert
    public void insert(ContactDetails contactDetails);

    @Update
    public void update(ContactDetails contactDetails);

    @Delete
    public void delete(ContactDetails contactDetails);

    @Query("DELETE FROM MyCustomContacts_Table")
    void deleteAllContacts();

    @Query("SELECT * FROM MyCustomContacts_Table ORDER BY id ASC")
    LiveData<List<ContactDetails>> getAllContacts();

    @Query("SELECT * from MyCustomContacts_Table")
    List<ContactDetails> getAllUsersContacts();

    @Query("UPDATE MyCustomContacts_Table SET selectedMobile=:selectedMobileYesNo WHERE phone = :mobileNo")
    void update1(String selectedMobileYesNo,String mobileNo);

    //@Query("SELECT phone, selectedMobile FROM MyCustomContacts_Table WHERE phone = :mobileno")
    //String fetch(String mobileno);

    //@Query("SELECT phone, selectedMobile FROM MyCustomContacts_Table WHERE phone = :mobileno")
    //List<ContactDetails> getContactsSelectedMobileNo(String mobileno);

}
