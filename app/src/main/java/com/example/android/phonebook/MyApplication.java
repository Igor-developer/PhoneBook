package com.example.android.phonebook;

import android.app.Application;

import com.example.android.phonebook.sqlite.DBHelper;
import com.example.android.phonebook.sqlite.SQLiteContactsManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Инциализация базы данных SQLite
        SQLiteContactsManager.init(new DBHelper(getApplicationContext()));
    }


}
