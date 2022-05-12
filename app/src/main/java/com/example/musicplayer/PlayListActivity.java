package com.example.musicplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayListActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayListActivity extends Fragment {
    DataBaseHelper db;
    Context context;
    List<String> list;
    ArrayAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlayListActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayListActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayListActivity newInstance(String param1, String param2) {
        PlayListActivity fragment = new PlayListActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_list_activity, container, false);
        context = container.getContext();
        db = new DataBaseHelper(context);

        // Inflate the layout for this fragment
        Button newPlayList = view.findViewById(R.id.newPlaylist);
        ListView listView = view.findViewById(R.id.lv_playlists);
        SwipeRefreshLayout pullToRefresh1 = view.findViewById(R.id.update);

        newPlayList.setOnClickListener(view12 -> {

            AlertDialog dialog;
            AlertDialog.Builder dialogBuilder;
            dialogBuilder = new AlertDialog.Builder(context);

            view12 = getLayoutInflater().inflate(R.layout.playlist_popup, null);
            Button btn_ok = view12.findViewById(R.id.buttonOk);
            EditText ed_listName = view12.findViewById(R.id.editTextTextPersonName);
            Button btn_cancel = view12.findViewById(R.id.buttonCancel);
            dialogBuilder.setView(view12);
            dialog = dialogBuilder.create();
            dialog.show();
            btn_ok.setOnClickListener(view3 -> {
                if (!ed_listName.getText().toString().isEmpty()) {
                    String ed_playlistName = ed_listName.getText().toString();
                    db.addPlaylist(ed_playlistName);
                    Toast.makeText(context, "Successfully added!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        });


        this.list = db.getPlayListName();
        if (list.size() != 0) {
            adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, db.getPlayListName());
            listView.setAdapter(adapter);
        }
        pullToRefresh1.setOnRefreshListener(() -> {

            adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, db.getPlayListName());
            listView.setAdapter(adapter);

            pullToRefresh1.setRefreshing(false);
        });

        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            Intent intent = new Intent(getActivity(), ListsOfSongsActivity.class);
            intent.putExtra("listNumber",i);
            startActivity(intent);

        });


        return view;
    }
}