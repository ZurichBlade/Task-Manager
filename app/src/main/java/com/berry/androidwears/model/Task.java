package com.berry.androidwears.model;

import java.io.Serializable;

// Task class representing a model for tasks
public class Task implements Serializable {

    // Properties of the Task class
    private String taskId;
    private String taskName;
    private String dueDateTime;

    // Constructor to initialize a Task object with provided values
    public Task(String taskId, String taskName, String dueDateTime) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.dueDateTime = dueDateTime;
    }

    // Getter method for taskId
    public String getTaskId() {
        return taskId;
    }

    // Setter method for taskId
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    // Getter method for taskName
    public String getTaskName() {
        return taskName;
    }

    // Setter method for taskName
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    // Getter method for dueDateTime
    public String getDueDateTime() {
        return dueDateTime;
    }

    // Setter method for dueDateTime
    public void setDueDateTime(String dueDateTime) {
        this.dueDateTime = dueDateTime;
    }
}
