package com.example.pool.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pool.R;
import com.example.pool.players.Player;
import com.example.pool.room.DBViewModel;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoardAdapter extends RecyclerView.Adapter<ScoreBoardAdapter.ScoreBoardViewHolder> {
    private List<Player> allPlayers=new ArrayList<>();
    private Player currentPlayer;
    private Context context;
    private DBViewModel mViewModel;

    public ScoreBoardAdapter(Player currentPlayer, Context context, DBViewModel dbViewModel) {
        this.currentPlayer=currentPlayer;
        this.mViewModel=dbViewModel;
        this.context=context;
    }

    @NonNull
    @Override
    public ScoreBoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.leader_board_item,parent,false);
        return new ScoreBoardViewHolder(v);
    }
    public void setPlayers(List<Player> allPlayers){
        this.allPlayers=allPlayers;
        this.currentPlayer=mViewModel.findCurrentPlayer();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreBoardViewHolder holder, int position) {
        Player player=allPlayers.get(position);
        holder.mPlayerName.setText(player.getPlayerName());
        holder.mPlayerPosition.setText(String.valueOf(player.getPlayerPosition()));
        holder.mPlayerScore.setText(String.valueOf(player.getPlayerScores()));
        if(player.equals(currentPlayer)){
            //update drawable resource
            holder.linearLayout.setBackground(context.getDrawable(R.drawable.corner_curve_current_player_screen_buttons));
        }

    }

    @Override
    public int getItemCount() {
        return allPlayers.size();
    }

    public class ScoreBoardViewHolder extends RecyclerView.ViewHolder {
        private TextView mPlayerPosition,mPlayerName,mPlayerScore;
        private LinearLayout linearLayout;
        ScoreBoardViewHolder(View v){
            super(v);
            mPlayerName=v.findViewById(R.id.player_name_tv);
            mPlayerPosition=v.findViewById(R.id.player_position_tv);
            mPlayerScore=v.findViewById(R.id.player_score_tv);
            linearLayout=v.findViewById(R.id.player_tile_background);
        }
    }
}
