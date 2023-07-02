package com.example.myloginapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity2 extends AppCompatActivity {

    ImageButton play, pause, stop;
    MediaPlayer player;
    boolean isPlaying = false;
    private static final String CHANNEL_ID = "media_player_notification_channel";
    private static final int NOTIFICATION_ID = 1;

    private static MainActivity2 instance;

    public static MainActivity2 getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        instance = this;

        createNotificationChannel();

        play=findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        stop = findViewById(R.id.stop);
        player = MediaPlayer.create(this, R.raw.b);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPlaying) {
                    player.start();
                    isPlaying = true;
                    showNotification();
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player != null && player.isPlaying()) {
                    player.pause();
                    isPlaying = false;
                    showNotification();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player != null && player.isPlaying()) {
                    player.pause();
                    player.seekTo(0);
                    isPlaying = false;
                    showNotification();
                }
            }
        });
    }

    private void showNotification() {
        Intent playIntent = new Intent(this, MediaPlayerBroadcastReceiver.class);
        playIntent.setAction("PLAY");
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent pauseIntent = new Intent(this, MediaPlayerBroadcastReceiver.class);
        pauseIntent.setAction("PAUSE");
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent stopIntent = new Intent(this, MediaPlayerBroadcastReceiver.class);
        stopIntent.setAction("STOP");
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Media Player")
                .setContentText("Playing audio")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(R.drawable.ic_baseline_play_arrow_24, "Play", playPendingIntent)
                .addAction(R.drawable.ic_baseline_pause_24, "Pause", pausePendingIntent)
                .addAction(R.drawable.ic_baseline_stop_24, "Stop", stopPendingIntent)
                .setAutoCancel(false);

        Notification notification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Media Player Notification Channel";
            String description = "Channel for Media Player Notifications";
            int importance = android.app.NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            android.app.NotificationManager notificationManager = getSystemService(android.app.NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    public void handleAction(String action){
        switch (action) {
            case "PLAY":
                if (!isPlaying) {
                    player.start();
                    isPlaying = true;
                    showNotification();
                }
                break;
            case "PAUSE":
                if (player != null && player.isPlaying()) {
                    player.pause();
                    isPlaying = false;
                    showNotification();
                }
                break;
            case "STOP":
                if (player != null && player.isPlaying()) {
                    player.pause();
                    player.seekTo(0);
                    isPlaying = false;
                    showNotification();
                }
                break;
        }
    }
}