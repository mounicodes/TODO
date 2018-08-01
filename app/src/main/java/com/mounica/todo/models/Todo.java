package com.mounica.todo.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.io.Serializable;

/**
 * Object that represents a Todotask as table in the Database
 */

@Entity
public class Todo implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int index;
    private String title;
    private String text;

    public Todo(String title, String text) {
        this.text = text;
        this.title = title;
    }

    public void updateTo(Todo todo) {
        if (index != todo.index) {
            throw new UnsupportedOperationException(
                    "Only the Todo with same index can be used for updates");
        }
        title = todo.title;
        text = todo.text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int hashCode() {
        return index * 457;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Todo)) {
            return false;
        }

        Todo thatTodo = (Todo) o;

        return thatTodo.index == index;
    }
}
