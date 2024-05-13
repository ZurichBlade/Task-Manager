package com.berry.androidwears;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.berry.androidwear.R;
import com.berry.androidwears.model.Task;
import com.berry.androidwears.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskCheckService extends Service {

    private Thread taskCheckThread;
    private List<Task> taskList;

    // Notification channel ID
    private static final String CHANNEL_ID = "tasks_channel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        taskList = (List<Task>) intent.getSerializableExtra("taskList");

        createNotificationChannel();
        startTaskCheckThread();

        return START_STICKY;
    }

    private void startTaskCheckThread() {
        taskCheckThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                checkTasksDueWithinOneHour(taskList);
                try {
                    Thread.sleep(60000); // Check every minute
                } catch (InterruptedException e) {
                    // Thread interrupted, break out of the loop
                    break;
                }
            }
        });
        taskCheckThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (taskCheckThread != null && taskCheckThread.isAlive()) {
            taskCheckThread.interrupt();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    // Check for tasks due within the next one hour
    private void checkTasksDueWithinOneHour(List<Task> list) {
        Calendar oneHourLater = Calendar.getInstance();
        oneHourLater.add(Calendar.HOUR, 1);

        list.clear();
        list.addAll(CommonUtils.getAllTasks(this));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy : hh:mm a", Locale.getDefault());


        for (Task task : list) {
            try {
                Date dueTime = dateFormat.parse(task.getDueDateTime());
                if (dueTime != null && dueTime.after(Calendar.getInstance().getTime()) && dueTime.before(oneHourLater.getTime())) {
                    // Task is due within the next hour, trigger notification
                    showNotification(task);
                }
            } catch (ParseException e) {
                Log.e("TAG", "ParseException >>>>" + e);
                e.printStackTrace();
            }
        }
    }

    // Show notification for a task due within the next one hour
    private void showNotification(Task task) {

        // Create an intent to launch the new screen
        Intent intent = new Intent(this, UpcomingTasksActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(getString(R.string.due_task))
                .setContentText("Task ID: " + task.getTaskId() + "\nTask Name: " + task.getTaskName())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Set the PendingIntent for the notification
                .addAction(R.mipmap.ic_launcher_round, "View Tasks", pendingIntent) // Add an action to launch the new screen
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(task.getTaskId().hashCode(), builder.build());
    }

    // Create a notification channel for tasks
    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}
