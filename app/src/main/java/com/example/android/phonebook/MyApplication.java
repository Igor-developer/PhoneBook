package com.example.android.phonebook;

import android.app.Application;
import androidx.room.Room;
import com.example.android.phonebook.room_db.PhoneBookDao;
import com.example.android.phonebook.room_db.PhoneBookDatabase;
import com.example.android.phonebook.room_db.RoomSingleton;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Инициализация базы данных Room
        PhoneBookDao room = Room.databaseBuilder(getApplicationContext(),
                PhoneBookDatabase.class, "room").allowMainThreadQueries().build()
                .getPhoneBookDao();

        RoomSingleton.init(room);
    }
}
