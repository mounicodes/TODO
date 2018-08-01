package com.mounica.todo.views;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.mounica.todo.DatabaseManager;
import com.mounica.todo.R;
import com.mounica.todo.models.Todo;
import com.mounica.todo.utils.Events.onCreateTask;
import com.mounica.todo.utils.Events.onDeleteTask;
import com.mounica.todo.utils.Events.onUpdateTask;

import org.greenrobot.eventbus.EventBus;

/**
 * This Activity is used to create a new task, edit & view the existing task.
 */
public class InputActivity extends AppCompatActivity {

    public static final String EXTRA_OPERATION_TYPE = "type";
    public static final String EXTRA_OPERATION_TYPE_UPDATE = "update";
    public static final String EXTRA_TODO = "todo";
    public static final String ALERT_YES = "yes";
    public static final String ALERT_NO = "no";

    private EditText mInput;
    private Toolbar mToolbar;

    private boolean mIsUpdate;
    private DatabaseManager mDatabaseManager = DatabaseManager.getInstance();
    private Todo mTodo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        // Setup the toolbar
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mInput = findViewById(R.id.edit_input);

        mIsUpdate = false;
        if (getIntent().getStringExtra(EXTRA_OPERATION_TYPE).equals(EXTRA_OPERATION_TYPE_UPDATE)) {
            mIsUpdate = true;
            mTodo = (Todo) getIntent().getSerializableExtra(EXTRA_TODO);
            mInput.setText(mTodo.getText());
            mInput.setSelection(mTodo.getText().length());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.save:
                boolean isOperationPerformed = true;
                if (mIsUpdate) {
                    if (mInput.getText().length() > 0) {
                        updateTodo();
                    } else {
                        deleteTodo();
                    }
                } else {
                    if (mInput.getText().length() > 0) {
                        createTodo();
                    } else {
                        isOperationPerformed = false;
                    }
                }
                // If we create, update or delete a item, we have to finish it and move back to the main activity
                if (isOperationPerformed) {
                    super.onBackPressed();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    // Handle back button Action Down
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!mIsUpdate) {
            // Empty text do nothing
            if (mInput.getText().toString().length() == 0) {
                super.onBackPressed();
            } else {
                showAlert();
            }
        } else {
            // Input not changed
            if (mTodo.getText().equals(mInput.getText().toString())) {
                super.onBackPressed();
            } else {
                showAlert();
            }
        }
    }

    // Show an alert dialog when back button is pressed save/discard the text accordingly
    private void showAlert() {
        new AlertDialog.Builder(this)
                .setNegativeButton(ALERT_NO,
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                InputActivity.super.onBackPressed();
                            }
                        })
                .setPositiveButton(ALERT_YES,
                        new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mIsUpdate) {
                                    if (mInput.getText().toString().length() == 0) {
                                        deleteTodo();
                                    } else {
                                        updateTodo();
                                    }
                                } else {
                                    createTodo();
                                }
                                InputActivity.super.onBackPressed();
                            }
                        })
                .setTitle(R.string.alert_to_save)
                .create()
                .show();
    }

    public void createTodo() {
        // Use the first word as title of the task
        String title = mInput.getText().toString().split("\\s|\\n", 2)[0];
        Todo todo = new Todo(title, mInput.getText().toString());

        // Create in DB and post event to Main Activity
        mDatabaseManager.create(todo);
        EventBus.getDefault().post(new onCreateTask(todo));
    }

    public void updateTodo() {
        // Use the first word as title of the task
        String title = mInput.getText().toString().split("\\s|\\n", 2)[0];
        mTodo.setTitle(title);
        mTodo.setText(mInput.getText().toString());

        // Update from DB and post event to Main Activity
        mDatabaseManager.update(mTodo);
        EventBus.getDefault().post(new onUpdateTask(mTodo));
    }

    private void deleteTodo() {
        // Delete from DB and post event to Main Activity
        mDatabaseManager.delete(mTodo);
        EventBus.getDefault().post(new onDeleteTask(mTodo));
    }
}
