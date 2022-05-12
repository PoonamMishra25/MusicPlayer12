package com.example.musicplayer.Notification;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.musicplayer.R;


public class CreateNotification {
    public static final String CHANNEL_ID = "Chennal_Id";
    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_PREV = "PREV";
    public static final String ACTION_NEXT = "NEXT";


    public static Notification notification;


    @SuppressLint("UnspecifiedImmutableFlag")
    public static void CreateNotification1(Context context, String title, String artist, int pos, int size) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");

            //  Bitmap bitmap=BitmapFactory.de);

            PendingIntent prev_pendingIntent;
            int prevImage;
            if (pos == 0) {
                prev_pendingIntent = null;
                prevImage = 0;
            } else {
                Intent intent = new Intent(context, NotificationActionService.class).setAction(ACTION_PREV);
                prev_pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                prevImage = R.drawable.ic_baseline_skip_previous_24;
            }

            Intent intentPlay = new Intent(context, NotificationActionService.class).setAction(ACTION_PLAY);
            PendingIntent play_pendingIntent = PendingIntent.getBroadcast(context, 0, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
            int playImage = R.drawable.ic_baseline_pause_circle_filled_24;


            PendingIntent next_pendingIntent;
            int nextImage;
            if (pos == size) {
                next_pendingIntent = null;
                nextImage = 0;
            } else {
                Intent intentNext = new Intent(context, NotificationActionService.class).setAction(ACTION_NEXT);
                next_pendingIntent = PendingIntent.getBroadcast(context, 0, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
                nextImage = R.drawable.ic_baseline_skip_next_24;
            }


            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_music_note_24)
                    .setContentTitle(title)
                    .setContentText(artist)
//                    .setLargeIcon(getEmbeddedPicture())
                    .setOnlyAlertOnce(true)
                    .addAction(prevImage, "Previous", prev_pendingIntent)
                    .addAction(playImage, "Play", play_pendingIntent)
                    .addAction(nextImage, "Next", next_pendingIntent)

                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build();

            notificationCompat.notify(12, notification);
        }
    }

    public static Bitmap getEmbeddedPicture() {
        String path = "/storage/3166-3362/Music/07_Ammi_Udeek_Di(Mr-Khan.com).mp3";
        Bitmap bitmap = null;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);

        byte[] data = mmr.getEmbeddedPicture();

        if (data != null) {
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        return bitmap;
    }

}
