package com.example.musicplayer;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class singleTonExample {


    private static singleTonExample ourInstance;
    DataBaseHelper db;
    boolean isPlaying;
    int id=1;
    int finalTime;
    int startTime;
    String play_from;
    SeekBar seekBar;
    TextView start1, tv_songName;
    Handler myHandler = new Handler();
    String paths, songName;
    AudioData audioData = new AudioData();
    private MediaPlayer mediaPlayer;

    private singleTonExample() {
        if (mediaPlayer == null)
            // it's ok, we can call this constructor
            mediaPlayer = new MediaPlayer();
        //  isPlaying=true;
    }


    public String playPause() {
        if (mediaPlayer != null)
        {
            if (!mediaPlayer.isPlaying())
            {
                mediaPlayer.start();
                this.isPlaying = true;
            }
            else
            {
                mediaPlayer.pause();
                this.isPlaying = false;
            }
        }
        return audioData.getSongName();

    }

    public void playSong(Context context, int id, String play_from)
    {
        this.id = id;
        db = new DataBaseHelper(context);
        this.play_from = play_from;


        setSongInMedia(context, id, play_from);
        if (mediaPlayer != null)
        {

            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();

                    mediaPlayer.reset();
                    this.isPlaying = false;
                }
                mediaPlayer.stop();

                mediaPlayer.reset();
                mediaPlayer.setDataSource((paths));
                mediaPlayer.prepare();
                mediaPlayer.start();
                this.isPlaying = true;
                mediaPlayer.setOnCompletionListener(mediaPlayer1 -> nextSong(context));
            }
            catch (IOException e) {e.printStackTrace();}

        }
        setSongName(audioData.getSongName());
        setName();

    }

    private void setSongInMedia(Context context, int id, String play_from)
    {
        this.id = id;
        db = new DataBaseHelper(context);
        if (play_from.equalsIgnoreCase("FromFav"))
        {
            audioData = db.getFavPaths(id);

        }
        else if (play_from.equalsIgnoreCase("FromplayList"))
        {
            audioData = db.getlistPaths(id);
        } else
        {
           audioData = db.getPaths(this.id);
        }
        if(audioData!=null) {
            paths = audioData.getPath();
        }
    }

    public void stopMedia()
    {
        if (mediaPlayer != null)
        {
            isPlaying = false;
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    public String nextSong(Context context)
    {

        db = new DataBaseHelper(context);
        AudioData next = db.getNext(id);
        int id1 = next.getId();

        if (mediaPlayer.isPlaying() || mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
            playSong(context, id1, play_from);
        }
        return audioData.getSongName();
    }

    public String prevSong(Context context) {

        db = new DataBaseHelper(context);
        AudioData prev = db.getPrevious(id);
        int id1 = prev.getId();
        if (!(id1 < 0))
        {
            if (mediaPlayer.isPlaying() || mediaPlayer != null)
            {
                mediaPlayer.stop();
                mediaPlayer.reset();
                playSong(context, id1, play_from);
            }
        }
        return audioData.getSongName();
    }


    public static synchronized singleTonExample getInstance() {
        if (ourInstance == null) {

            ourInstance = new singleTonExample();
        }
        return ourInstance;
    }


    @SuppressLint("DefaultLocale")
    public void seekToSeekbar(TextView start1, TextView end) {
        seekBar = getSeekBar();
        if (mediaPlayer != null) {
            finalTime = mediaPlayer.getDuration();
            this.startTime = mediaPlayer.getCurrentPosition();
            seekBar.setMax(finalTime);

            end.setText(String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(finalTime),
                    TimeUnit.MILLISECONDS.toSeconds(finalTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                            toMinutes(finalTime)))
            );
            start1.setText(String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(startTime),
                    TimeUnit.MILLISECONDS.toSeconds(startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes(startTime)))
            );
            seekBar.setProgress(startTime);
            myHandler.postDelayed(UpdateSongTime, 1000);
        }
    }

    private final Runnable UpdateSongTime = new Runnable() {
        @SuppressLint("DefaultLocale")
        public void run() {

            startTime = mediaPlayer.getCurrentPosition();
            getStart1().setText(String.format("%02d:%02d ",
                    TimeUnit.MILLISECONDS.toMinutes(startTime),
                    TimeUnit.MILLISECONDS.toSeconds(startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes(startTime)))
            );
            seekBar.setProgress(startTime);
            myHandler.postDelayed(this, 1000);

        }
    };


    public void setSongName(String name) {
        this.songName = name;
        songPlaying();
    }

    public String getSongName() {
        return songName;
    }


    public void setTv_songName(TextView tv_songName) {
        this.tv_songName = tv_songName;
    }

    public TextView getTv_songName() {
        return tv_songName;
    }

    public void setName() {

        getTv_songName().setText(getSongName());
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public TextView getStart1() {
        return start1;
    }

    public void setTextview(TextView start1) {
        this.start1 = start1;
    }

    public void saveSharedPreference(Context context)
    {
        SharedPreferences sh = context.getSharedPreferences("FROMLIST", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();
        myEdit.putString("From", this.play_from);
        myEdit.putInt("songId", this.id);
        myEdit.putString("currPosition", String.valueOf(mediaPlayer.getCurrentPosition()));
        myEdit.apply();
    }

//    public void fetchSharedPref(Context context)
//    {
//
//        SharedPreferences sh = context.getSharedPreferences("FROMLIST", Context.MODE_PRIVATE);
//        this.play_from = sh.getString("From", "");
//        this.id = sh.getInt("songId", 1);
//
//        this.startTime = Integer.parseInt(sh.getString("currPosition", "0"));
//        setSongInMedia(context, id, play_from);
//        mediaPlayer.reset();
//        try {
//            mediaPlayer.setDataSource((paths));
//            mediaPlayer.prepare();
//            mediaPlayer.seekTo(startTime);
//
//        }
//        catch (IOException e) { e.printStackTrace();}
//
//
//    }

    public boolean songPlaying() {
       return  mediaPlayer.isPlaying();
    }

}
