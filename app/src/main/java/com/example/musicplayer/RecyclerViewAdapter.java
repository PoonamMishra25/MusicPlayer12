package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.myViewHolder> implements PopupMenu.OnMenuItemClickListener {
    private List<AudioData> list;
    Context context;
    AudioData audioData;

    public RecyclerViewAdapter(List<AudioData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.tvName.setText(list.get(position).getSongName());
        holder.tvArtist.setText(list.get(position).getArtist());
        holder.menuOptionsBtn.setOnClickListener(view -> {
           // audioData = getItem(position);

            final long itemId = getItemId(position);
            int itemViewType = getItemViewType(position);
            Toast.makeText(context, ""+itemId, Toast.LENGTH_SHORT).show();
//            PopupMenu popupMenu = new PopupMenu(context, view22);
//            popupMenu.setOnMenuItemClickListener(RecyclerViewAdapter.this);
//            popupMenu.inflate(R.menu.menu_items);
//            popupMenu.show();
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvArtist;
        CardView cardView;
        ImageButton menuOptionsBtn;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.titleName);
            tvArtist = itemView.findViewById(R.id.artist);
            cardView = itemView.findViewById(R.id.cardView);
            menuOptionsBtn=itemView.findViewById(R.id.menuOptions);
        }
    }
    
}
