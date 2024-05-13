package com.berry.androidwears;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.berry.androidwear.R;
import com.berry.androidwear.databinding.ActivityAddTaskBinding;
import com.berry.androidwears.model.Task;
import com.berry.androidwears.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity implements View.OnClickListener {

    // View binding
    ActivityAddTaskBinding binding;

    // List to store tasks
    List<Task> list = new ArrayList<>();

    // Calendar to manage date and time
    private final Calendar selectedDateTime = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Disable the "Add Task" button initially
        binding.buttonAddTask.setEnabled(false);

        // Set click listeners for buttons
        binding.buttonAddTask.setOnClickListener(this);
        binding.editTextDueDate.setOnClickListener(this);

    }

    // Update task in the list and save to storage
    private void updateTask(Task task) {
        CommonUtils.saveTask(task, this);
        list.addAll(CommonUtils.getAllTasks(this));
    }

    // Create a new task with the given parameters
    private Task createTask(String id, String task, String duetime) {
        if (id == null) {
            id = String.valueOf(System.currentTimeMillis());
        }
        return new Task(id, task, duetime);
    }


    // Initialize the task with user input
    private void addTask() {
        // Get task name from the EditText
        String taskName = binding.editTextTaskName.getText().toString().trim();
        String taskId = binding.editTextTaskId.getText().toString().trim();
        if (!TextUtils.isEmpty(taskName) || !TextUtils.isEmpty(taskId)) {
            // Create a new task and update the list


            String date = convertDate(String.valueOf(selectedDateTime.getTime()));

            Task task = createTask(taskId, taskName, date);
            if (CommonUtils.isTaskIdExist(task, this)) {
                Toast.makeText(this, "Please enter different task id.", Toast.LENGTH_SHORT).show();
            } else {
                updateTask(task);
                Toast.makeText(AddTaskActivity.this, R.string.task_saved, Toast.LENGTH_SHORT).show();
                //navigate back to listing
                super.onBackPressed();
            }

        } else {
            // Display a message if the task name is empty
            Toast.makeText(AddTaskActivity.this, R.string.please_set_due_date_time, Toast.LENGTH_SHORT).show();
        }
    }

    public static String convertDate(String dateString) {
        SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);  //Fri Mar 22 10:30:15 GMT 2024
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy : hh:mm a", Locale.ENGLISH);//22 Mar 2024 : 10:30 PM
        try {
            Date date = originalFormat.parse(dateString);
            assert date != null;
            return targetFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Handle button clicks
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonAddTask) {
            addTask();
        }
        if (view.getId() == R.id.editTextDueDate) {
            showDateTimePicker();
        }
    }

    private void showDateTimePicker() {
        // DatePickerDialog logic
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedDateTime.set(Calendar.YEAR, year);
                selectedDateTime.set(Calendar.MONTH, month);
                selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // Show TimePickerDialog after selecting the date
                showTimePicker();
            }
        }, selectedDateTime.get(Calendar.YEAR), selectedDateTime.get(Calendar.MONTH), selectedDateTime.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    // Show time picker dialog
    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                selectedDateTime.set(Calendar.MINUTE, minute);

                binding.buttonAddTask.setEnabled(true);
                //set date to edittext
                String date = convertDate(String.valueOf(selectedDateTime.getTime()));
                binding.editTextDueDate.setText(date);

            }
        }, selectedDateTime.get(Calendar.HOUR_OF_DAY), selectedDateTime.get(Calendar.MINUTE), true // 24-hour format
        );
        timePickerDialog.show();
    }
}