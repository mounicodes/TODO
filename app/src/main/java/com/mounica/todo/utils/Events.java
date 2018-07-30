package com.mounica.todo.utils;

import com.mounica.todo.models.Todo;
import java.util.ArrayList;
import java.util.List;

public class Events {

  // Event to handle DB Query to get all the tasks
  public static class onDataLoad {

    private List<Todo> mTodoList;

    public onDataLoad(List<Todo> todoList) {
      mTodoList = new ArrayList<>();
      mTodoList.addAll(todoList);
    }

    public List<Todo> getTodoList() {
      return mTodoList;
    }
  }

}
