package com.example.android.phonebook;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ContactsManager {

    private List<Entry> contactList;

    //Паттерн Singleton
    private static ContactsManager contactsManager;

    private ContactsManager() {
        this.contactList = new ArrayList<>();
    }

    public static ContactsManager getInstance() {
        if (contactsManager == null) {
            contactsManager = new ContactsManager();
        }

        return contactsManager;
    }

    //Получение количества записей
    public int getSize() {
        return contactList.size();
    }

    //Содержится ли запись с данным именем
    public boolean containsPerson(String name) {
        for (Entry i : contactList) {
            if (i.person.equals(name)) {
                return true;
            }
        }

        return false;
    }

    //Получение записи по имени человека
    public Entry getEntry(String name) {
        for (Entry i : contactList) {
            if (i.getPerson().equals(name)) {
                return i;
            }
        }

        return null;
    }

    //Получение записи по индексу
    public Entry getEntry(int index) {
        try {
            return contactList.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    //Добавление записи
    public void addEntry(String name, String phone) {
        contactList.add(new Entry(name, phone));
    }

    public void replace(int index, Entry replacement) {
        contactList.set(index, replacement);
    }

    //Внутренний класс, который содержит контакты человека
    public static class Entry {
        private String person;
        private String phone;

        public Entry(String person, String phone) {
            this.person = person;
            this.phone = phone;
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
    }
}