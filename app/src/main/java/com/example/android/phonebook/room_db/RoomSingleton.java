package com.example.android.phonebook.room_db;

public class RoomSingleton {

    private static RoomSingleton roomSingleton;
    private static PhoneBookDao room;

    private RoomSingleton(PhoneBookDao room) {
        RoomSingleton.room = room;
    }

    public static void init(PhoneBookDao room) {
        if (roomSingleton == null) {
            roomSingleton = new RoomSingleton(room);
        }
    }

    public static PhoneBookDao getRoom() {
        return room;
    }
}
