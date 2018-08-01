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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Launcher activity that displays the existing list of {@link com.mounica.todo.models.Todo}
 */
public class MainActivity extends AppCompatActivity {

    private DatabaseManager mDatabaseManager = DatabaseManager.getInstance();
    private MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Register for Events through EventBus
        EventBus.getDefault().register(this);

        RecyclerView recyclerView = findViewById(R.id.rv_todo_holder);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MainAdapter();
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTodoIntent = new Intent(view.getContext(), InputActivity.class);
                newTodoIntent.putExtra(InputActivity.EXTRA_OPERATION_TYPE, InputActivity.OPERATION_TYPE_CREATE);
                startActivity(newTodoIntent);
            }
        });

        // Get all the existing todos from the DB
        mDatabaseManager.getAllTodos();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPerformQueryEvent(Events.onDataLoad onDataLoad) {
        mAdapter.initializeWithList(onDataLoad.getTodoList());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreateEvent(Events.onCreateTask onCreateTask) {
        mAdapter.addItem(onCreateTask.getTodo());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateEvent(Events.onUpdateTask onUpdateTask) {
        mAdapter.updateItem(onUpdateTask.getTodo());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteEvent(Events.onDeleteTask onDeleteTask) {
        mAdapter.deleteItem(onDeleteTask.getTodo());
    }
}
