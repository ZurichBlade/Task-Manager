package com.berry.androidwears;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.berry.androidwear.databinding.ActivityMainBinding;
import com.berry.androidwears.adapter.TaskAdapter;
import com.berry.androidwears.model.Task;
import com.berry.androidwears.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvTaskList.setLayoutManager(layoutManager);

        // Set click listener for adding new task
        binding.imgAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTaskActivity.class);
            startActivity(intent);
        });

        // Start the background service to check for tasks due within the next hour if there are tasks
        if (!taskList.isEmpty()) {
            startTaskCheckService();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh task list when the activity is resumed
        getTaskFromPref();
    }

    private void getTaskFromPref() {
        // Retrieve task list from SharedPreferencesManager
        taskList = CommonUtils.getAllTasks(this);

        // Update UI based on task list availability
        if (taskList.isEmpty()) {
            binding.rvTaskList.setVisibility(View.GONE);
            binding.textView.setVisibility(View.VISIBLE);
        } else {
            binding.rvTaskList.setVisibility(View.VISIBLE);
            binding.textView.setVisibility(View.GONE);
        }

        // Create and set adapter with retrieved task data
        TaskAdapter adapter = new TaskAdapter(this, taskList);
        binding.rvTaskList.setAdapter(adapter);

        // Start the background service to check for tasks due within the next hour if there are tasks
        if (!taskList.isEmpty()) {
            startTaskCheckService();
        }
    }

    private void startTaskCheckService() {
        Intent serviceIntent = new Intent(this, TaskCheckService.class);
        serviceIntent.putExtra("taskList", new ArrayList<>(taskList)); // Passing a copy of the list
        startService(serviceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the background service when the activity is destroyed
        stopService(new Intent(this, TaskCheckService.class));
    }
}


