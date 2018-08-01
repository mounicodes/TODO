package com.mounica.todo.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

/**
 * Data Access Object for the Todotask
 */
@Dao
public interface TodoDao {

    @Query("SELECT * FROM Todo")
    List<Todo> getAll();

    @Delete
    void deleteTodoTask(Todo todo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createTodoTask(Todo todo);

    @Update
    void updateTodoTask(Todo todo);
}
