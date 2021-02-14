package com.example.android.phonebook;

import java.util.ArrayList;
import java.util.List;

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

    //Поиск всех записей по частичному совпадению
    public List<Entry> findEntries(String query) {
        List<Entry> findings = new ArrayList<>();
        for (int i = 0; i < contactList.size(); i++) {
            Entry entry = contactList.get(i);
            entry.listIndex = i;    //установка "оригинального" индекса
            if (query == null || entry.person.toLowerCase().contains(query.toLowerCase())) {
                findings.add(entry);
            }
        }

        return findings;
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

    public void replaceEntry(int index, Entry replacement) {
        contactList.set(index, replacement);
    }

    public void removeEntry(int index) {
        contactList.remove(index);
    }

    //Внутренний класс, который содержит контакты человека
    public static class Entry {
        private String person;
        private String phone;
        private int listIndex;

        public Entry(String person, String phone) {
            this.person = person;
            this.phone = phone;
        }

        public String getPerson() {
            return person;
        }

        public String getPhone() {
            return phone;
        }

        public int getListIndex() {
            return listIndex;
        }
    }
}