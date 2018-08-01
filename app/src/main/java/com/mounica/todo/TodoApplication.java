package com.mounica.todo;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.mounica.todo.models.TodoDatabase;

/**
 * Custom Application class for an initial database build. This reference is being used whenever database instance is
 * required.
 */

public class TodoApplication extends Application {

    private static TodoDatabase sTodoDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        sTodoDatabase = Room
                .databaseBuilder(getApplicationContext(), TodoDatabase.class, TodoDatabase.NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public static TodoDatabase getMyDatabase() {
        return sTodoDatabase;
    }
}
