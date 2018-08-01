package com.mounica.todo;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

import com.mounica.todo.utils.Events.onCreateTask;
import com.mounica.todo.utils.Events.onDataLoad;
import com.mounica.todo.models.Todo;

import org.greenrobot.eventbus.EventBus;

/**
 * Singleton Database Manager that performs Create/Update/Delete operations on the database table in a worker thread
 * and posts the events to the respective subscribers.
 */

public class DatabaseManager {

    private static final String THREAD_NAME = "database_worker";
    private static DatabaseManager sDatabaseManager;

    private HandlerThread mWorkerThread;
    private Handler mHandler;

    public static DatabaseManager getInstance() {
        if (sDatabaseManager == null) {
            sDatabaseManager = new DatabaseManager();
        }
        return sDatabaseManager;
    }

    private DatabaseManager() {
        mWorkerThread = new HandlerThread(THREAD_NAME);
        mWorkerThread.start();
        mHandler = new Handler(mWorkerThread.getLooper());
    }

    // Performs Database query to read all the todoitems and posts an event
    public void getAllTodos() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new onDataLoad(TodoApplication.getMyDatabase().todoDao().getAll()));
            }
        });
    }

    // Creates new todoitem in the database and posts an event
    public void create(@NonNull final Todo todo) {
        Preconditions.checkNotNull(todo);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                long id = TodoApplication.getMyDatabase().todoDao().createTodoTask(todo);
                todo.setIndex((int) id);
                EventBus.getDefault().post(new onCreateTask(todo));
            }
        });
    }

    // Updates todoitem in the database
    public void update(@NonNull final Todo todo) {
        Preconditions.checkNotNull(todo);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TodoApplication.getMyDatabase().todoDao().updateTodoTask(todo);
            }
        });
    }

    // Deletes todoitem in the database
    public void delete(@NonNull final Todo todo) {
        Preconditions.checkNotNull(todo);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TodoApplication.getMyDatabase().todoDao().deleteTodoTask(todo);
            }
        });
    }
}
