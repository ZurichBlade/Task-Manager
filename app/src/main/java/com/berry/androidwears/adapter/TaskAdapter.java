package com.berry.androidwears.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableRecyclerView;

import com.berry.androidwear.R;
import com.berry.androidwear.databinding.TaskItemBinding;
import com.berry.androidwears.model.Task;

import java.util.List;

public class TaskAdapter extends WearableRecyclerView.Adapter<TaskAdapter.ViewHolder> {
    // Context for accessing resources and UI elements
    private final Context context;

    // List of tasks to display
    private final List<Task> taskList;

    // Constructor to initialize the adapter with context and task list
    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create a ViewHolder
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TaskItemBinding binding = TaskItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Format task information and set it to TextViews in the ViewHolder
        String formattedTaskName = context.getString(R.string.b_taskname_b) + taskList.get(position).getTaskName();
        String formattedTaskID = context.getString(R.string.b_taskid_b) + taskList.get(position).getTaskId();
        String formattedDtimeDate = context.getString(R.string.b_duetime_date_b) + taskList.get(position).getDueDateTime();

        holder.binding.textViewTaskId.setText(Html.fromHtml(formattedTaskID));
        holder.binding.textViewTaskName.setText(Html.fromHtml(formattedTaskName));
        holder.binding.textViewDueDateTime.setText(Html.fromHtml(formattedDtimeDate));
    }

    // Return the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // ViewHolder class to hold references to the UI elements in each item view
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // View binding for the item layout
        private final TaskItemBinding binding;

        // Constructor to initialize the ViewHolder with the binding
        public ViewHolder(@NonNull TaskItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
