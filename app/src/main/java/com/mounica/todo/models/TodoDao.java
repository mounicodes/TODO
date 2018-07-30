package com.mounica.todo.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface TodoDao {

  @Query("SELECT * FROM Todo")
  List<Todo> getAll();

  @Delete
  void deleteTodoTask(Todo todo);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void createTodoTask(Todo todo);
}
