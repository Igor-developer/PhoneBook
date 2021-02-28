package com.example.android.phonebook.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PhoneBookSQLite.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE = "directory";
    public static final String ID = "id";
    public static final String PERSON = "person";
    public static final String PHONE = "phone";
    public static final String CHOSEN = "chosen";
    public static final String ADD_TIME = "addtime";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Создание таблицы в базе данных
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + PERSON + " TEXT NOT NULL,"
                + PHONE + " TEXT,"
                + CHOSEN + " INTEGER,"
                + ADD_TIME + " INTEGER);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Просто удаляем старую таблицу и создаём заново
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
