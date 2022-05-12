package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import java.util.List;

public class favAdapter extends ArrayAdapter<AudioData> {

    Context context;
    DataBaseHelper db;

    List<AudioData> favList;



    public favAdapter(@NonNull Context context, int resource , List<AudioData> list) {
        super(context, resource, list);
        this.favList = list;
        this.context = context;

    }

    public View getView(int position, View view, ViewGroup parent) {
       this.context  = parent.getContext();


        @SuppressLint("ViewHolder") View view1 = LayoutInflater.from(context).inflate(R.layout.favlist, null, false);
        TextView songName = view1.findViewById(R.id.favtitle);



        ImageView thumbnailImage = view1.findViewById(R.id.favimage);
        CardView cardView = view1.findViewById(R.id.favcardView);

        db=new DataBaseHelper(context);
        //favList = db.getFavTitles();



            thumbnailImage.setImageResource(R.drawable.ic_baseline_music_note_24);

       songName.setText(favList.get(position).getSongName());

        cardView.setOnClickListener(view2 -> {
           // Intent intent = new Intent(context, oneSongPlay.class);
//            SharedPreferences sh= context.getSharedPreferences("FROMLIST", Context.MODE_PRIVATE);
//            SharedPreferences.Editor myEdit = sh.edit();
//            myEdit.putString("From","FromFav");
//            myEdit.apply();
//            String song = sh.getString("From", "");
//            intent.putExtra("id", favList.get(position).getId());
//            intent.putExtra("fromSong", song);
//
//
//            context.startActivity(intent);
            singleTonExample.getInstance().playSong(context,favList.get(position).getId(),"FromFav");
        });








        return view1;
    }


}
