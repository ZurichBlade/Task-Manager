package com.berry.androidwears.utils;


import static com.berry.androidwears.UpcomingTasksActivity.customDateFormat;

import android.content.Context;
import android.content.SharedPreferences;

import com.berry.androidwears.model.Task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonUtils {

    // Save a task to SharedPreferences
    public static void saveTask(Task task, Context context) {
        if (task != null) {
            SharedPreferences preferences = context.getSharedPreferences("task_details", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(task.getTaskId(), task.getTaskName() + "|" + task.getDueDateTime());
            editor.apply();
        }
    }

    public static boolean isTaskIdExist(Task task, Context context) {
        if (task != null) {
            SharedPreferences preferences = context.getSharedPreferences("task_details", Context.MODE_PRIVATE);
            // Check if the task ID already exists
            return preferences.contains(task.getTaskId());
        }
        return false;
    }

    // Get all tasks from SharedPreferences
    public static List<Task> getAllTasks(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("task_details", Context.MODE_PRIVATE);
        List<Task> taskList = new ArrayList<>();
        Map<String, ?> map = sharedPref.getAll();
        Set set = map.entrySet();
        Iterator itr = set.iterator();
        while (itr.hasNext()) {
            Map.Entry entry = (Map.Entry) itr.next();
            String savedTask = (String) entry.getValue();
            if (savedTask != null) {
                String[] taskDetails = savedTask.split("\\|");
                if (taskDetails.length == 2) {
                    Task task = new Task(entry.getKey().toString(), taskDetails[0], taskDetails[1]);
                    taskList.add(task);
                }
            }
        }
        return taskList;
    }


    // Get upcoming tasks within one hour from the provided list
    public static List<Task> getUpcomingTasksWithinOneHour(List<Task> allTasks) {
        List<Task> filteredTasks = new ArrayList<>();

        Calendar currentTime = Calendar.getInstance();
        Calendar oneHourFromNow = Calendar.getInstance();
        oneHourFromNow.add(Calendar.HOUR_OF_DAY, 1);

        for (Task task : allTasks) {
            try {
                Date dueDate = customDateFormat.parse(task.getDueDateTime());

                if (dueDate.after(currentTime.getTime()) && dueDate.before(oneHourFromNow.getTime())) {
                    filteredTasks.add(task);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return filteredTasks;
    }
}