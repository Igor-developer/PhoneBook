package com.example.android.phonebook.room_db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;

@Entity
public class Entry {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String person;

    private String phone;

    private boolean chosen;

    @ColumnInfo(name = "add_time")
    private long addTime;

    public Entry(String person, String phone) {
        this.person = person;
        this.phone = phone;
        this.chosen = false;
        this.addTime = new Date().getTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }
}