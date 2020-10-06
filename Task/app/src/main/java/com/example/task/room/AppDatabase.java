package com.example.task.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Metro.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MetroDAO metroDAO();

    private static AppDatabase INSTANCE;

    private static final Object sLock = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class , "metro.db" )
                        .allowMainThreadQueries()
                        .build();
            }
            return INSTANCE;
        }
    }

}
