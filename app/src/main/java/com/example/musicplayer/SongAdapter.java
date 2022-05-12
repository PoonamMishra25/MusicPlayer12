package com.example.musicplayer;

import static com.example.musicplayer.singleTonExample.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.example.musicplayer.Notification.CreateNotification;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SongAdapter extends ArrayAdapter<AudioData> implements PopupMenu.OnMenuItemClickListener {

    Activity context;
    DataBaseHelper db;
    List<AudioData> audioDataList;

    int position1;
    AudioData audioData;

    ImageButton imageButton;

    public SongAdapter(@NonNull Activity context, int resource, List<AudioData> list) {
        super(context, resource, list);
        this.audioDataList = list;
        this.context = context;

    }

    public int getPosition1() {
        return position1;
    }

    public void setPosition1(int position1) {
        this.position1 = position1;
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint("ViewHolder") View view1 = inflater.inflate(R.layout.list_item, parent, false);
        TextView tvSongName = view1.findViewById(R.id.titleName);
        TextView tvArtist = view1.findViewById(R.id.artist);
        imageButton = view1.findViewById(R.id.menuOptions);


        ImageView thumbnailImage = view1.findViewById(R.id.image);
        CardView cardView = view1.findViewById(R.id.cardView);
        db = new DataBaseHelper(context);

        thumbnailImage.setImageResource(R.drawable.ic_baseline_music_note_24);

        tvSongName.setText(audioDataList.get(position).getSongName());
        tvArtist.setText(audioDataList.get(position).getArtist());

        imageButton.setOnClickListener(view22 -> {

            audioData = getItem(position);
            PopupMenu popupMenu = new PopupMenu(context, view22);
            popupMenu.setOnMenuItemClickListener(SongAdapter.this);
            popupMenu.inflate(R.menu.menu_items);
            popupMenu.show();

        });


        cardView.setOnClickListener(view2 -> {
            setPosition1(position);
            getInstance().playSong(context, audioDataList.get(position).getId(), "FromSong");
            CreateNotification.CreateNotification1(context,audioDataList.get(position).getSongName(),audioDataList.get(position).getArtist()
            ,position,audioDataList.size());

        });


        return view1;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.addPlayList:
                pop();
                return true;

            case R.id.fav:

                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                db.addFavOne(audioData.getSongName(), audioData.getArtist(), audioData.getPath(), currentDate);
                Toast.makeText(context, "Favorite Song added!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.delete:
                db.deleteOne(audioData.getId());
                Toast.makeText(context, audioData.getSongName() + " has been deleted!", Toast.LENGTH_SHORT).show();
            default:
                return false;

        }
    }

    public void pop() {
        AlertDialog dialog;
        AlertDialog.Builder dialogBuilder;
        dialogBuilder = new AlertDialog.Builder(context);

        View view = context.getLayoutInflater().inflate(R.layout.popup_playlists, null);
        Button createNew = view.findViewById(R.id.button);
        createNew.setOnClickListener(view1 -> {

            AlertDialog dialog1;
            AlertDialog.Builder dialogBuilder1;
            dialogBuilder1 = new AlertDialog.Builder(context);

            view1 = context.getLayoutInflater().inflate(R.layout.playlist_popup, null);
            Button btn_ok = view1.findViewById(R.id.buttonOk);
            EditText ed_listName = view1.findViewById(R.id.editTextTextPersonName);
            Button btn_cancel = view1.findViewById(R.id.buttonCancel);
            dialogBuilder1.setView(view1);
            dialog1 = dialogBuilder1.create();
            dialog1.show();
            btn_ok.setOnClickListener(view3 -> {
                if (!ed_listName.getText().toString().isEmpty()) {
                    String ed_playlistName = ed_listName.getText().toString();
                    db.addPlaylist(ed_playlistName);
                    Toast.makeText(context, "Successfully added!", Toast.LENGTH_SHORT).show();
                    dialog1.dismiss();
                }
            });
            btn_cancel.setOnClickListener(view2 -> dialog1.dismiss());

        });

        ListView listView = view.findViewById(R.id.lists_name);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, db.getPlayListName());
        listView.setAdapter(adapter);

        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.show();

        listView.setOnItemClickListener((adapterView, view1, i, l) -> {

            db.insertSongsPlaylist(audioData.getSongName(), i, audioData.getPath());
            Toast.makeText(context, "The song has been added!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

        });

    }
}
