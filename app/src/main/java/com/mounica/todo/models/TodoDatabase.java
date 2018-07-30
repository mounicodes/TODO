package com.mounica.todo.models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Todo.class},version = 2)
public abstract class TodoDatabase extends RoomDatabase{

  public abstract TodoDao todoDao();

  public static final String NAME = "TodoDatabase";

}
