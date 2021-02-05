package com.example.android.phonebook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Contacts {

    private static Map<String, Set<String>> contacts;
    private static List<PreparedText> contactList;

    //Паттерн Singleton
    private Contacts() {
    }

    public static Map getContactsAsMap() {
        if (contacts == null) {
            contacts = new TreeMap<>();
        }

        return contacts;
    }

    //Преобразование Map в ArrayList
    public static List<PreparedText> getContactsAsList() {
        contactList = new ArrayList<>();

        for (Map.Entry<String, Set<String>> i :
                ((Map<String, Set<String>>) getContactsAsMap()).entrySet()) {
            String person_text = i.getKey();

            StringBuilder telephones_text = new StringBuilder();
            Set<String> telephones = i.getValue();
            int n = 0;//счётчик необходим, чтобы не выводить лишний перенос строки
            for (String m : telephones) {
                telephones_text.append(m);
                telephones_text.append(++n == telephones.size() ? "" : System.lineSeparator());
            }

            PreparedText pt = new PreparedText(person_text, telephones_text.toString());

            contactList.add(pt);
        }

        return contactList;
    }

    //Внутренний класс, который содержит контакты человека в виде двух строк
    public static class PreparedText {
        private String person;
        private String telephones;

        public PreparedText(String person, String telephones) {
            this.person = person;
            this.telephones = telephones;
        }

        public String getPerson() {
            return person;
        }

        public String getTelephones() {
            return telephones;
        }
    }
}