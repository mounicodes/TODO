package com.mounica.todo.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.mounica.todo.DatabaseManager;
import com.mounica.todo.R;
import com.mounica.todo.models.Todo;
import com.mounica.todo.views.InputActivity;
import com.mounica.todo.views.MainActivity;
import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final String TAG = "MainAdapter";
  private List<Todo> mTodoList;
  private DatabaseManager mDatabaseManager = DatabaseManager.getInstance();

  public MainAdapter() {
    mTodoList = new ArrayList<>();
  }

  public void addItems(List<Todo> todoList) {
    mTodoList.clear();
    mTodoList.addAll(todoList);
    notifyDataSetChanged();
  }

  @Override
  public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.holder_todo, parent, false);
    final TodoViewHolder viewHolder = new TodoViewHolder(view, new OptionListener() {
      @Override
      public void onDelete(int position) {
        mDatabaseManager.delete(mTodoList.get(position));
        Intent intent = new Intent(parent.getContext(), MainActivity.class);
        parent.getContext().startActivity(intent);
      }

      @Override
      public void onEdit(int position) {
        Intent intent = new Intent(parent.getContext(), InputActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("todo", mTodoList.get(position));
        intent.putExtra("todo", bundle);
        intent.putExtra("type", "update");
        parent.getContext().startActivity(intent);
      }
    });
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Todo todo = mTodoList.get(position);
    TodoViewHolder viewHolder = (TodoViewHolder) holder;
    viewHolder.text.setText(todo.getText());
    viewHolder.title.setText(todo.getTitle());
  }

  @Override
  public int getItemCount() {
    return mTodoList.size();
  }

  private static class TodoViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

    private TextView title;
    private TextView text;
    private Button deleteButton;
    private Button editButton;
    private OptionListener optionListener;

    public TodoViewHolder(View itemView, OptionListener listener) {
      super(itemView);
      optionListener = listener;
      title = itemView.findViewById(R.id.text_title);
      text = itemView.findViewById(R.id.text_text);
      deleteButton = itemView.findViewById(R.id.button_delete);
      editButton = itemView.findViewById(R.id.button_edit);
      deleteButton.setOnClickListener(this);
      editButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()) {
        case R.id.button_delete:
          optionListener.onDelete(getAdapterPosition());
          break;
        case R.id.button_edit:
          optionListener.onEdit(getAdapterPosition());
          break;
        default:
          break;
      }
    }
  }

  public interface OptionListener {

    void onDelete(int position);

    void onEdit(int position);
  }

}
