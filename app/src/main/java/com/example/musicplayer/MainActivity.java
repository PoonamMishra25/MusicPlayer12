package com.example.musicplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.musicplayer.Notification.CreateNotification;
import com.example.musicplayer.Notification.Playable;
import com.example.musicplayer.Notification.onClearFromRecentService;
import com.example.musicplayer.databinding.ActivityMainBinding;
import com.example.musicplayer.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements Playable {

    private static final int READ_EXTERNAL_STORAGE = 1;
    ActionBarDrawerToggle actionBarDrawerToggle;
    static List<AudioData> songList;
    NotificationManager notificationManager;
    int positionClicked = 1;
    TextView songTitle;
    TextView tvTotal;
    String song_From, songName;
  //  RecyclerViewAdapter adapter;
    ImageButton playBtn;
    DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
    com.example.musicplayer.databinding.ActivityMainBinding binding;
    Handler handler;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(binding.getRoot());
        tvTotal = findViewById(R.id.tv_total);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        Toolbar toolbar = binding.toolbar1;
        setSupportActionBar(toolbar);

        //  Objects.requireNonNull(getSupportActionBar()).hide();


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerNav, toolbar, R.string.open, R.string.close);
        binding.getRoot().addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TabLayout tabs = binding.tabs;
        songTitle = binding.mainPageSongNAme;
        playBtn = binding.playMain;
        ImageButton nextBtn = binding.playNext;
        ImageButton prevBtn = binding.playPrev;
        ImageButton goToList = binding.goToList;
        tabs.setupWithViewPager(viewPager);





        checkPermission();
        songList = loadSongs(this);

       // singleTonExample.getInstance().fetchSharedPref(getApplication());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChennal();
            registerReceiver(broadcastReceiver,new IntentFilter("Tracks"));
            startService(new Intent(getBaseContext(), onClearFromRecentService.class));
        }

//        CreateNotification.CreateNotification1(MainActivity.this
//                ,songName
//                ,dataBaseHelper.getSongTitles().get(positionClicked).getArtist()
//                ,positionClicked, songList.size() - 1);

       // playPauseDisplay();

     //   tvTotal.setText( + "/" + songList.size());

      //  Toast.makeText(this, "" + songList.size(), Toast.LENGTH_SHORT).show();

        //add to database
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        for (int i = 0; i < songList.size(); i++) {
            dataBaseHelper.addOne(songList.get(i).getSongName(), songList.get(i).getArtist(), songList.get(i).getPath(),
                    date);
        }


        playBtn.setOnClickListener(view ->
        {
            songName = singleTonExample.getInstance().playPause();
            playPauseDisplay();
            songTitle.setText(songName);
        });



        singleTonExample.getInstance().setTv_songName(songTitle);
        songTitle.setSelected(true);


        nextBtn.setOnClickListener(view ->
        {
            songName = singleTonExample.getInstance().nextSong(getApplication());
            playPauseDisplay();
            songTitle.setText(songName);
        });

        prevBtn.setOnClickListener(view ->
        {
            songName = singleTonExample.getInstance().prevSong(getApplication());
            playPauseDisplay();
            songTitle.setText(songName);
        });


        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                singleTonExample.getInstance().saveSharedPreference(MainActivity.this);
                handler.postDelayed(this, 1000);

            }
        }, 5000);

        goToList.setOnClickListener(view -> {


            Intent intent = new Intent(this, oneSongPlay.class);
            intent.putExtra("name",songTitle.getText().toString());
            startActivity(intent);


        });


        //        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());

    }
    private void createChennal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CreateNotification.CHANNEL_ID, "Tracks",
                    NotificationManager.IMPORTANCE_LOW);
            //   notificationManager = getSystemService(NotificationManager.class);
             notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionName");

            switch (action) {
                case CreateNotification.ACTION_PREV:
                    onPrev();
                    break;

                case CreateNotification.ACTION_PLAY:
//                    if () {
//                        onPause();
//                    } else {
                        onPlay();
//                    }
                    break;
                case CreateNotification.ACTION_NEXT:
                    onNext();
                    break;
            }
        }
    };



    private void playPauseDisplay() {
        if (singleTonExample.getInstance().songPlaying()) {
            playBtn.setBackgroundResource(R.drawable.ic_baseline_pause_circle_filled_24);
        } else {
            playBtn.setBackgroundResource(R.drawable.ic_baseline_play_circle_filled_24);
        }
    }

    private ArrayList<AudioData> loadSongs(Context context) {
        ArrayList<AudioData> musicList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projections = {MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM};

        Cursor cursor = context.getContentResolver().query(uri, projections, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                musicList.add(new AudioData(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }
            cursor.close();
        }
        return musicList;
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            notificationManager.cancelAll();
        }
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onPrev() {
singleTonExample.getInstance().prevSong(getApplication());
    }

    @Override
    public void onPlay() {
        singleTonExample.getInstance().playPause();
    }

    @Override
    public void onNext() {
    singleTonExample.getInstance().nextSong(getApplication());
    }

    @Override
    public void onPause1() {

    }
}