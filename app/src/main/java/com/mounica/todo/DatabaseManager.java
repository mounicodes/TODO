package com.mounica.todo;

import android.os.Handler;
import android.os.HandlerThread;
import com.mounica.todo.utils.Events;
import com.mounica.todo.utils.Events.onDataLoad;
import com.mounica.todo.models.Todo;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class DatabaseManager {

  private static final String TAG = "DatabaseManager";
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
    mWorkerThread = new HandlerThread("worker");
    mWorkerThread.start();
    mHandler = new Handler(mWorkerThread.getLooper());
  }

  public void performQuery() {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        List<Todo> todoList = TodoApplication.getMyDatabase().todoDao().getAll();
        Events.onDataLoad onDataLoad = new onDataLoad(todoList);
        EventBus.getDefault().post(onDataLoad);
      }
    };
    mHandler.post(runnable);
  }

  public void create(Todo todo) {
    final Todo taskTodo = todo;
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        TodoApplication.getMyDatabase().todoDao().createTodoTask(taskTodo);
      }
    };
    mHandler.post(runnable);
  }

  public void update(Todo todo) {
    final Todo taskTodo = todo;
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        TodoApplication.getMyDatabase().todoDao().createTodoTask(taskTodo);
      }
    };
    mHandler.post(runnable);
  }

  public void delete(Todo todo) {
    final Todo taskTodo = todo;
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        TodoApplication.getMyDatabase().todoDao().deleteTodoTask(taskTodo);
      }
    };
    mHandler.post(runnable);
  }
}
