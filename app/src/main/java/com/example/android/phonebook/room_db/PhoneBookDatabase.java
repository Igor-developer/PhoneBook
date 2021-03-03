package com.example.android.phonebook.room_db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Entry.class}, version = 1)
public abstract class PhoneBookDatabase extends RoomDatabase {

    public abstract PhoneBookDao getPhoneBookDao();
}
