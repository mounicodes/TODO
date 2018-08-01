package com.mounica.todo.adapter;

import android.content.Intent;
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

/**
 * Recyclerview adapter that loads/updates data in the view
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private DatabaseManager mDatabaseManager = DatabaseManager.getInstance();
    private List<Todo> mTodoList = new ArrayList<>();

    // Loads data into the list
    public void initializeWithList(List<Todo> todoList) {
        mTodoList.addAll(todoList);
        notifyDataSetChanged();
    }

    // Adds new item at the end of the list
    public void addItem(Todo todo) {
        mTodoList.add(todo);
        notifyItemInserted(mTodoList.size());
    }

    // Updates item at the corresponding position
    public void updateItem(Todo todo) {
        int position = 0;
        for (Todo oldTodo : mTodoList) {
            if (todo.equals(oldTodo)) {
                oldTodo.updateTo(todo);
                notifyItemChanged(position);
                break;
            }
            position++;
        }
    }

    // Deletes item at the corresponding position
    public void deleteItem(Todo todo) {
        int position = 0;
        for (Todo oldTodo : mTodoList) {
            if (todo.equals(oldTodo)) {
                mTodoList.remove(position);
                notifyItemRemoved(position);
                break;
            }
            position++;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_todo, parent, false);
        final TodoViewHolder viewHolder = new TodoViewHolder(view, new UserActionListener() {
            @Override
            public void onDelete(int position) {
                // Delete from DB and update the view
                mDatabaseManager.delete(mTodoList.get(position));
                deleteItem(mTodoList.get(position));
            }

            @Override
            public void onEdit(int position) {
                // Load Input Activity with the selected item
                Intent intent = new Intent(parent.getContext(), InputActivity.class);
                intent.putExtra(InputActivity.EXTRA_TODO, mTodoList.get(position));
                intent.putExtra(InputActivity.EXTRA_OPERATION_TYPE, InputActivity.EXTRA_OPERATION_TYPE_UPDATE);
                parent.getContext().startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Todo todo = mTodoList.get(position);
        TodoViewHolder viewHolder = (TodoViewHolder) holder;
        viewHolder.mTextView.setText(todo.getText());
    }

    @Override
    public int getItemCount() {
        return mTodoList.size();
    }

    private static class TodoViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private TextView mTextView;
        private Button mDeleteButton;
        private UserActionListener mUserActionListener;

        public TodoViewHolder(View itemView, UserActionListener listener) {
            super(itemView);
            mUserActionListener = listener;
            mTextView = itemView.findViewById(R.id.text_text);
            mDeleteButton = itemView.findViewById(R.id.button_delete);
            mDeleteButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button_delete) {
                mUserActionListener.onDelete(getAdapterPosition());
            } else {
                mUserActionListener.onEdit(getAdapterPosition());
            }
        }
    }

    /**
     * Interface to be implemented inorder to listen to any actions to delete or edit an item
     */
    public interface UserActionListener {

        void onDelete(int position);

        void onEdit(int position);
    }

}
