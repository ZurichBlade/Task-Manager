package com.berry.androidwears;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.berry.androidwear.databinding.ActivityUpcomingTasksBinding;
import com.berry.androidwears.adapter.TaskAdapter;
import com.berry.androidwears.model.Task;
import com.berry.androidwears.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class UpcomingTasksActivity extends AppCompatActivity {

    // View binding
    ActivityUpcomingTasksBinding binding;

    // Date format for task timestamps
    public static final SimpleDateFormat customDateFormat = new SimpleDateFormat("dd MMM yyyy : hh:mm a", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        binding = ActivityUpcomingTasksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve upcoming tasks within one hour
        List<Task> upcomingTasks = getUpcomingTasksWithinOneHour();

        // Set up RecyclerView with the adapter
        binding.rvUpTaskList.setLayoutManager(new LinearLayoutManager(UpcomingTasksActivity.this));
        TaskAdapter adapter = new TaskAdapter(UpcomingTasksActivity.this, upcomingTasks);
        binding.rvUpTaskList.setAdapter(adapter);
    }

    // Retrieve upcoming tasks within one hour
    private List<Task> getUpcomingTasksWithinOneHour() {
        // Get all tasks from storage
        List<Task> allTasks = CommonUtils.getAllTasks(this);

        // Filter tasks to get those upcoming within one hour
        return CommonUtils.getUpcomingTasksWithinOneHour(allTasks);
    }
}