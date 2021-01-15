package com.example.pool.gui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pool.R;
import com.example.pool.players.Player;
import com.example.pool.room.DBViewModel;

import java.util.List;

public class ScoresBoardFragment extends Fragment {
    private DBViewModel mViewModel;
    private RecyclerView leaderBoardRecyclerView;

    public static ScoresBoardFragment newInstance(DBViewModel dbViewModel) {
        ScoresBoardFragment fragment = new ScoresBoardFragment();
        fragment.mViewModel=dbViewModel;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_board_score,container,false);
        leaderBoardRecyclerView=v.findViewById(R.id.score_board_recycler_view);
        Player currentPlayer=mViewModel.findCurrentPlayer();
        final ScoreBoardAdapter scoreBoardAdapter=new ScoreBoardAdapter(currentPlayer,getContext(),mViewModel);
        leaderBoardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        leaderBoardRecyclerView.setAdapter(scoreBoardAdapter);
        leaderBoardRecyclerView.setHasFixedSize(true);
        
        mViewModel.getThePlayersLive().observe(getActivity(), new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                scoreBoardAdapter.setPlayers(players);
            }
        });
        return v;
    }
}
