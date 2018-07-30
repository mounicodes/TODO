package com.mounica.todo.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.mounica.todo.DatabaseManager;
import com.mounica.todo.utils.Events;
import com.mounica.todo.adapter.MainAdapter;
import com.mounica.todo.R;
import com.mounica.todo.models.Todo;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private MainAdapter mAdapter;
  private List<Todo> mTodoList = new ArrayList<>();
  private DatabaseManager mDatabaseManager = DatabaseManager.getInstance();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Register for Events through EventBus
    EventBus.getDefault().register(this);

    FloatingActionButton fab = findViewById(R.id.fab);
    RecyclerView recyclerView = findViewById(R.id.rv_todo_holder);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
        false);
    recyclerView
        .setLayoutManager(layoutManager);
    mAdapter = new MainAdapter();
    recyclerView.setAdapter(mAdapter);

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent newTodoIntent = new Intent(view.getContext(), InputActivity.class);
        newTodoIntent.putExtra("type", "create");
        startActivity(newTodoIntent);
      }
    });

    // Perform background operations
    mDatabaseManager.performQuery();
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onPerformQueryEvent(Events.onDataLoad onDataLoad) {
    mTodoList.addAll(onDataLoad.getTodoList());
    mAdapter.addItems(mTodoList);
  }
}
