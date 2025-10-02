package com.example.assignment3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    boolean isPlaying = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isPlaying) {
            mediaPlayer = MediaPlayer.create(this, R.raw.song);
            mediaPlayer.start();
            isPlaying = true;

            showNotification();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int total = mediaPlayer.getDuration();
                    MainActivity.prog.setMax(total);

                    while (mediaPlayer != null) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {}

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (mediaPlayer != null && isPlaying && mediaPlayer.isPlaying()) {
                                    int pos = mediaPlayer.getCurrentPosition();
                                    MainActivity.prog.setProgress(pos);
                                    MainActivity.timeTxt.setText(pos / 1000 + " s");
                                }
                            }
                        });
                    }

                }
            }).start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        }
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification() {
        String channelId = "music_channel";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Music Channel", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        Intent stopIntent = new Intent(this, MyService.class);
        PendingIntent stopPending = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle("Music Player")
                .setContentText("Music is playing")
                .setSmallIcon(R.drawable.ic_music)
                .addAction(R.drawable.ic_stop, "Stop", stopPending)
                .build();

        startForeground(1, notification);
    }
}
