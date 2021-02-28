package com.example.android.phonebook.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class SQLiteContactsManager {

    SQLiteDatabase db;
    //Паттерн Singleton
    private static SQLiteContactsManager contactsManager;

    private SQLiteContactsManager(DBHelper dBHelper) {
        db = dBHelper.getWritableDatabase();
    }

    //Инициализация базы данных
    public static void init(DBHelper dBHelper) {
        if (contactsManager == null) {
            contactsManager = new SQLiteContactsManager(dBHelper);
        }
    }

    //Получение экземпляра SQLiteContactsManager, ранее инициализированного в методе init,
    //который вызывается в классе MyApplication при старте приложения
    public static SQLiteContactsManager getInstance() {

        return contactsManager;
    }

    //Получение количества записей
    public int getSize() {
        db.beginTransaction();
        int size = (int) DatabaseUtils.queryNumEntries(db, DBHelper.TABLE);
        db.setTransactionSuccessful();
        db.endTransaction();

        return size;
    }

    //Поиск всех записей по частичному совпадению или всех по запросу null
    public List<Entry> findEntries(String query) {
        db.beginTransaction();
        Cursor c;
        if (query == null) {
            c = db.query(DBHelper.TABLE, null, null,
                    null, null, null, DBHelper.PERSON);
        } else {
            c = db.rawQuery("SELECT * FROM " + DBHelper.TABLE
                            + " WHERE LOWER(" + DBHelper.PERSON + ") LIKE LOWER(?)" +
                            " ORDER BY " + DBHelper.PERSON,
                    new String[] {"%" + query + "%"});
        }
        db.setTransactionSuccessful();
        db.endTransaction();

        return parseCursor(c);
    }

    //Получение последних записей
    public List<Entry> findLastEntries(int count) {
        db.beginTransaction();
        Cursor c = db.query(DBHelper.TABLE, null, null, null,
                null, null,
                DBHelper.ADD_TIME + " DESC", String.valueOf(count));
        db.setTransactionSuccessful();
        db.endTransaction();

        return parseCursor(c);
    }

    //Поиск всех записей в зависимости от того входит в избранные или нет
    public List<Entry> selectByChosen(boolean isChoosen) {
        String query = isChoosen ? "1" : "0";

        db.beginTransaction();
        Cursor c = db.query(DBHelper.TABLE, null,
                DBHelper.CHOSEN + "= ?", new String[] {query},
                null, null, DBHelper.PERSON);
        db.setTransactionSuccessful();
        db.endTransaction();

        return parseCursor(c);
    }

    //Получение записи по имени человека
    public Entry getEntry(String person) {
        db.beginTransaction();
        Cursor c = db.query(DBHelper.TABLE, null,
                DBHelper.PERSON + "= ?", new String[] {person},
                null, null, null);
        db.setTransactionSuccessful();
        db.endTransaction();

        List<Entry> entries = parseCursor(c);

        return entries.size() == 0 ? null : entries.get(0);
    }

    //Получение записи по индексу
    public Entry getEntry(int id) {
        db.beginTransaction();
        Cursor c = db.query(DBHelper.TABLE, null,
                DBHelper.ID + "= ?", new String[] {String.valueOf(id)},
                null, null, null);
        db.setTransactionSuccessful();
        db.endTransaction();

        List<Entry> entries = parseCursor(c);

        return entries.size() == 0 ? null : entries.get(0);
    }

    //Добавление записи
    public void addEntry(String person, String phone) {
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.PERSON, person);
        cv.put(DBHelper.PHONE, phone);
        cv.put(DBHelper.CHOSEN, 0);
        cv.put(DBHelper.ADD_TIME, new Date().getTime());
        db.insert(DBHelper.TABLE, null, cv);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //Переписывание записи
    public void replaceEntry(int id, String person, String phone) {
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.PERSON, person);
        cv.put(DBHelper.PHONE, phone);
        db.update(DBHelper.TABLE, cv, DBHelper.ID + " = ?",
                new String[] {String.valueOf(id)});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //Добавление и удаление из избранных
    public void setChosen(boolean isChoosen, int id) {
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.CHOSEN, isChoosen ? 1 : 0);
        db.update(DBHelper.TABLE, cv, DBHelper.ID + " = ?",
                new String[] {String.valueOf(id)});
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    //Удаление записи
    public void removeEntry(int index) {
        db.beginTransaction();
        db.delete(DBHelper.TABLE, DBHelper.ID + " = ?",
                new String[] {String.valueOf(index)});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //Вспомогательный метод для парсинга содержимого Cursor
    private List<SQLiteContactsManager.Entry> parseCursor(Cursor c) {
        List<SQLiteContactsManager.Entry> findings = new ArrayList<>();

        if (c != null && c.moveToFirst()) {
            do {
                SQLiteContactsManager.Entry entry =
                        new SQLiteContactsManager.Entry(
                                c.getString(c.getColumnIndex(DBHelper.PERSON)),
                                c.getString(c.getColumnIndex(DBHelper.PHONE)));
                entry.entryId = c.getInt(c.getColumnIndex(DBHelper.ID));
                entry.choosen = c.getInt(c.getColumnIndex(DBHelper.CHOSEN)) != 0;
                entry.addTime = c.getInt(c.getColumnIndex(DBHelper.ADD_TIME));
                findings.add(entry);
            } while (c.moveToNext());
        }

        c.close();

        return findings;
    }

    //Внутренний класс, который содержит контакты человека
    public static class Entry {

        private int entryId;
        private String person;
        private String phone;
        private boolean choosen;
        private long addTime;

        public Entry(String person, String phone) {
            this.person = person;
            this.phone = phone;
        }

        public int getEntryId() {
            return entryId;
        }

        public String getPerson() {
            return person;
        }

        public String getPhone() {
            return phone;
        }

        public boolean isChoosen() {
            return choosen;
        }

        public void setChoosen(boolean choosen) {
            this.choosen = choosen;
        }

        public long getAddTime() {
            return addTime;
        }
    }
}