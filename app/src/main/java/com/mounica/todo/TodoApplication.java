package com.mounica.todo;

import android.app.Application;
import android.arch.persistence.room.Room;
import com.mounica.todo.models.TodoDatabase;

public class TodoApplication extends Application {

  private static TodoApplication sTodoApplication;
  private static TodoDatabase sTodoDatabase;

  public TodoApplication() {
    sTodoApplication = this;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    sTodoDatabase = Room
        .databaseBuilder(getApplicationContext(), TodoDatabase.class, TodoDatabase.NAME)
        .fallbackToDestructiveMigration()
        .build();
  }

  public static TodoApplication getInstance() {
    return sTodoApplication;
  }

  public static TodoDatabase getMyDatabase() {
    return sTodoDatabase;
  }
}
