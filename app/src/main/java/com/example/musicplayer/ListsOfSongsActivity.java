package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListsOfSongsActivity extends AppCompatActivity {
DataBaseHelper db;
listsAdpater adpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_of_songs);
        ListView listView=findViewById(R.id.lists);
        TextView textView=findViewById(R.id.textView2);
        ImageButton backbtn=findViewById(R.id.imageButton);

        Objects.requireNonNull(getSupportActionBar()).hide();

        db=new DataBaseHelper(this);

        int listnumber=getIntent().getIntExtra("listNumber",0);
        List<AudioData> list1= db.getPerticularListSongs(listnumber);
     //   ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,list1);
        adpater=new listsAdpater(this,R.layout.lists_songs,list1);

        String str=db.listName(listnumber+1);
        textView.setText(str);

        listView.setAdapter(adpater);
        backbtn.setOnClickListener( view -> {
            finish();
        });

    }
}