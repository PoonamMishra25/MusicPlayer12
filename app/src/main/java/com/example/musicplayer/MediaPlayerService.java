package com.example.musicplayer;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Random;

class MusicPlayerService extends Service {

    MediaPlayer mediaPlayer;
    boolean isPlayerReady = false;
    private IBinder myBinder = new myBinder();
    private static final String TAG = "MusicPlayerService";
    String path = "";
    List<AudioData> songList;
    int pos=1;
    DataBaseHelper db=new DataBaseHelper(getBaseContext());
    AudioData audioData;
    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        songList = MainActivity.songList;
        Random random=new Random();
        pos=random.nextInt(songList.size()+1);
        //To know media player is ready and music file loaded
        mediaPlayer.setOnPreparedListener(mp -> {
            isPlayerReady = true;
            Log.d(TAG, "media_is_ready");
        });

        //resetting MediaPlayer is always good.
        mediaPlayer.reset();

        // mediaPlayer.setDataSource("/storage/emulated/0/Android/media/com.google.android.gm/Notifications/Shrink_ray/Shrink Ray.ogg");
        mediaPlayer = MediaPlayer.create(getBaseContext(), Uri.parse(songList.get(pos).getPath()));
        //You can prepare the mediaplayer syncrhonised or asynchronised.
        //chose one of them.
        // mediaPlayer.prepare();
        // mediaPlayer.prepareAsync();

        //After it I strongly recommend to create a listener to when the setted music playing is completed.
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "music_completed");
                pos +=1;

                // music is completed and you can go the next music
            }
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return (IBinder) myBinder;
    }

    class myBinder extends Binder {


        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }
}
