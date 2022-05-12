package com.example.musicplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class oneSongPlay extends AppCompatActivity {

    AudioData audioData = new AudioData();

    Button stop1, play, next, prev, lyric_Btn;
    SeekBar seekBar;
    TextView start1, end1, tv_name, tv_artist;

    long totalIds;
    int currentId;
    ImageButton btn_fav;
    String songFrom = "";

    ImageView pic;
    String name1, artist1;
    boolean buttonPressed = false;
    DataBaseHelper dataBaseHelper = new DataBaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_song_play);
        stop1 = findViewById(R.id.stop1);
        play = findViewById(R.id.play1);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        start1 = findViewById(R.id.startTim);
        lyric_Btn = findViewById(R.id.lyric);
        end1 = findViewById(R.id.endTim);
        seekBar = findViewById(R.id.seekBar2);
        btn_fav = findViewById(R.id.btn_fav);


        tv_name = findViewById(R.id.name);
        tv_artist = findViewById(R.id.artist);

        songFrom = getIntent().getStringExtra("fromSong");

        currentId = getIntent().getIntExtra("id", 0);
        pic = findViewById(R.id.mediaImage);

        tv_name.setText(name1);
        tv_artist.setText(artist1);
        totalIds = dataBaseHelper.getMaxId();

        singleTonExample.getInstance().setSeekBar(seekBar);

        try {
            getEmbeddedPicture();
        }
        catch (Exception e) {
            Toast.makeText(this, "Error Occured! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        play.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_24);

        btn_fav.setOnClickListener(view -> {

            btn_fav.setImageResource(R.drawable.ic_baseline_favorite_24);
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            dataBaseHelper.addFavOne(audioData.getSongName(), audioData.getArtist(), audioData.getPath(), currentDate);


            Toast.makeText(this, "Favorite list has been updated!", Toast.LENGTH_SHORT).show();

        });
        prev.setOnClickListener(view -> {

            singleTonExample.getInstance().prevSong(getApplicationContext());
            playPauseDisplay();
            getEmbeddedPicture();
        });
        next.setOnClickListener(view -> {
            singleTonExample.getInstance().nextSong(getApplicationContext());
            playPauseDisplay();
            getEmbeddedPicture();
        });
        play.setOnClickListener(view -> {
            singleTonExample.getInstance().playPause();
            playPauseDisplay();
            getEmbeddedPicture();
        });


        singleTonExample.getInstance().seekToSeekbar(start1, end1);
        singleTonExample.getInstance().setTextview(start1);

        stop1.setOnClickListener(view -> singleTonExample.getInstance().stopMedia());


        lyric_Btn.setOnClickListener(view -> {
            buttonPressed = true;

            getLyrics(name1);

        });


    }

    private void getLyrics(String name1) {
        try {
            name1=getIntent().getStringExtra("name");
            String url1 = "https://www.google.com/search?q= " + name1 + " lyrics" ;

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url1));
            startActivity(i);

        } catch (Exception e) {
            Toast.makeText(this, "Error Loading! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    void getEmbeddedPicture() {
        audioData = singleTonExample.getInstance().audioData;

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();

        mmr.setDataSource(audioData.getPath());

        byte[] data = mmr.getEmbeddedPicture();

        if (data != null) {
            //   image = BitmapFactory.decodeByteArray(data, 0, data.length);
            pic.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
        } else {
            pic.setImageResource(R.drawable.ic_baseline_music_note_24);
        }

        tv_name.setText(audioData.getSongName());

    }

    private void playPauseDisplay() {
        if (singleTonExample.getInstance().songPlaying()) {
            play.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_24);
        } else {
            play.setBackgroundResource(R.drawable.ic_baseline_play_circle_filled_24);
        }
    }
}

