package com.example.pool.gui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pool.R;
import com.example.pool.players.Player;

import java.util.ArrayList;
import java.util.List;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.PlayerHolder> {
    private List<Player> thePlayers = new ArrayList<>();
    private ItemTouchListener listener;

    public void setPlayers(List<Player> players) {
        thePlayers = players;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new PlayerHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerHolder holder, int position) {
        Player player = thePlayers.get(position);
        holder.name.setText(player.getPlayerName());
        holder.index.setText(String.valueOf(player.getPlayerIndex()));
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onTouchItem(player);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return thePlayers.size();
    }

    class PlayerHolder extends RecyclerView.ViewHolder {
        private TextView index, name;
        private ImageButton removeBtn;

        public PlayerHolder(View v) {
            super(v);
            index = v.findViewById(R.id.index);
            name = v.findViewById(R.id.player_name);
            removeBtn = v.findViewById(R.id.remove_player);
        }
    }

    interface ItemTouchListener {
        void onTouchItem(Player player);
    }

    public void setItemTouchListener(ItemTouchListener listener) {
        this.listener = listener;
    }
}
