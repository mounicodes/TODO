package com.mounica.todo.utils;

import com.mounica.todo.models.Todo;

import java.util.ArrayList;
import java.util.List;

/**
 * Events collection class
 */
public class Events {

    /**
     * Event to handle DB Query to get all the tasks
     */
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

    /**
     * Event to handle DB Create
     */
    public static class onCreateTask {

        private Todo mTodo;

        public onCreateTask(Todo todo) {
            mTodo = todo;
        }

        public Todo getTodo() {
            return mTodo;
        }
    }

    /**
     * Event to handle DB Update
     */
    public static class onUpdateTask {

        private Todo mTodo;

        public onUpdateTask(Todo updatedTodo) {
            mTodo = updatedTodo;
        }

        public Todo getTodo() {
            return mTodo;
        }
    }

    /**
     * Event to handle DB Delete
     */
    public static class onDeleteTask {

        private Todo mTodo;

        public onDeleteTask(Todo todo) {
            mTodo = todo;
        }

        public Todo getTodo() {
            return mTodo;
        }
    }

}
