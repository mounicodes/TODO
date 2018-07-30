package com.mounica.todo.views;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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

/**
 * This Activity is used to save the task
 */
public class InputActivity extends AppCompatActivity {

  private DatabaseManager mDatabaseManager = DatabaseManager.getInstance();

  private EditText mInput;
  private Todo mTodo;
  private Toolbar mToolbar;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_input);
    mInput = findViewById(R.id.edit_input);
    if (getIntent().getStringExtra("type").equals("update")) {
      Bundle bundle = getIntent().getBundleExtra("todo");
      mTodo = (Todo) bundle.getSerializable("todo");
      mInput.setText(mTodo.getText());
    }

    // Setup the toolbar
    mToolbar = findViewById(R.id.toolbar);
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        saveTodo();
        break;
      default:
        return super.onOptionsItemSelected(item);
    }
    return true;
  }

  // Handle back button Action Down
  @Override
  public boolean onSupportNavigateUp() {
    showAlert();
    return true;
  }

  // Show an alert dialog when back button is pressed save/discard the text accordingly
  private void showAlert() {
    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    alertDialog.setTitle(R.string.alert_to_save);
    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.yes),
        new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            saveTodo();
          }
        });
    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.no),
        new OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            onBackPressed();
          }
        });
    alertDialog.show();
  }

  public void saveTodo() {

    // Use the first word as title of the task
    String title = mInput.getText().toString().split(" ", 2)[0];
    Todo todo = new Todo(title, mInput.getText().toString());

    // Update
    if (mTodo != null) {
      todo.setIndex(mTodo.getIndex());
      mDatabaseManager.update(todo);
    }
    // Create
    else {
      mDatabaseManager.create(todo);
    }
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }
}
