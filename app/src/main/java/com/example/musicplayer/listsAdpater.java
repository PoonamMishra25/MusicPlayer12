package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import java.util.List;

public class listsAdpater extends ArrayAdapter<AudioData> {

    Context context;
    DataBaseHelper db;

    List<AudioData> listsSongs;
    int listid = 0;


    public listsAdpater(@NonNull Context context, int resource, List<AudioData> list) {
        super(context, resource, list);
        this.listsSongs = list;
        this.context = context;

    }

    public View getView(int position, View view, ViewGroup parent) {
        this.context = parent.getContext();


        @SuppressLint("ViewHolder") View view1 = LayoutInflater.from(context).inflate(R.layout.lists_songs, parent, false);
        TextView songName = view1.findViewById(R.id.textView3);


        ImageView thumbnailImage = view1.findViewById(R.id.imageView);
        CardView cardView = view1.findViewById(R.id.cardList);

        db = new DataBaseHelper(context);


        thumbnailImage.setImageResource(R.drawable.ic_baseline_music_note_24);

        songName.setText(listsSongs.get(position).getSongName());

        cardView.setOnClickListener(view2 -> {


//            SharedPreferences sh = context.getSharedPreferences("FROMLIST", Context.MODE_PRIVATE);
//            SharedPreferences.Editor myEdit = sh.edit();
//            myEdit.putString("From", "FromplayList");
//            myEdit.apply();
//            String song = sh.getString("From", "");
            singleTonExample.getInstance().playSong(context, listsSongs.get(position).getId(), "FromplayList");

        });


        return view1;
    }


}
