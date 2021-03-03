package com.example.android.phonebook.room_db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PhoneBookDao {

    //Получение количества записей
    @Query("SELECT COUNT(*) FROM entry")
    int getEntriesCount();

    //Получение записи по индексу
    @Query("SELECT * FROM entry WHERE id = :id")
    Entry getEntry(int id);

    //Получение записи по имени человека
    @Query("SELECT * FROM entry WHERE person = :person")
    Entry getEntry(String person);

    //Поиск всех записей
    @Query("SELECT * FROM entry ORDER BY person")
    List<Entry> getAllEntries();

    //Поиск всех записей по частичному совпадению
    @Query("SELECT * FROM entry WHERE LOWER(person) LIKE LOWER( '%' || :query || '%') ORDER BY person")
    List<Entry> findEntries(String query);

    //Получение последних записей
    @Query("SELECT * FROM entry ORDER BY add_time DESC LIMIT :count")
    List<Entry> getLastEntries(int count);

    //Поиск всех записей в зависимости от того входят они в избранные или нет
    @Query("SELECT * FROM entry WHERE chosen = :isChosen ORDER BY person")
    List<Entry> selectByChosen(boolean isChosen);

    //Добавление записи
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addEntry(Entry entry);

    //Обновление записи
    @Update
    void updateEntry(Entry entry);

    //Удаление записи
    @Delete
    void deleteEntry(Entry entry);
}
