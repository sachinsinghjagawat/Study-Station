package com.example.studystation.studentNotes;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationClass extends Application {

    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }
    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Vote Of Thanks",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This notification is for thanking the user who uploaded his notes");
            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Notes Update",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.setDescription("This is for updating the person that a new note is uploaded on the App");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
        }
    }

}
