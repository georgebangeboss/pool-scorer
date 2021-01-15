package com.example.pool.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.pool.R;
import com.example.pool.Utils;
import com.example.pool.action.Action;
import com.example.pool.players.Player;
import com.example.pool.room.DBViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.pool.gui.PreviewAdapter.*;

public class Preview extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private DBViewModel mViewModel;
    private ImageButton forwardButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview);
        Log.d(TAG, "onCreate: " + "Created");

        forwardButton = findViewById(R.id.forward);
        backButton = findViewById(R.id.back);

        mViewModel = new ViewModelProvider(this).get(DBViewModel.class);

        Intent intent = getIntent();
        ArrayList<String> incomingPlayersStrings;
        if (intent.hasExtra(InputScreen.PLAYER_NAMES)) {
            incomingPlayersStrings = intent.getStringArrayListExtra(InputScreen.PLAYER_NAMES);
            List<Player> createdPlayers = Player.createInitialPlayers(incomingPlayersStrings);
            for (Player player : createdPlayers) {
                mViewModel.insertPlayer(player);
            }
        }
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPlayersFinal(mViewModel);
                createInitialEvent(mViewModel);
                goToCurrentPlayerScreen();
                //once you go forward there's no going back
                finish();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.deleteAllPlayers();
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_viewer_preview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        PreviewAdapter mAdapter = new PreviewAdapter();
        recyclerView.setAdapter(mAdapter);

        mViewModel.getThePlayersLive().observe(this, new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                mAdapter.setPlayers(players);
            }
        });
        mAdapter.setItemTouchListener(new ItemTouchListener() {
            @Override
            public void onTouchItem(Player player) {
                mViewModel.deletePlayer(player);
            }
        });
    }

    private void goToCurrentPlayerScreen() {
        Intent intent=new Intent(Preview.this,CurrentPlayerScreen.class);
        startActivity(intent);
    }

    private void editPlayersFinal(DBViewModel dbViewModel) {
        List<Player> playersFromDB = dbViewModel.getPlayersList();
        List<String> playerNames = new ArrayList<>();
        for (Player player : playersFromDB) {
            playerNames.add(player.getPlayerName());
        }
        dbViewModel.deleteAllPlayers();
        List<Player> createdPlayers = Player.createInitialPlayers(playerNames);
        for (Player player : createdPlayers) {
            dbViewModel.insertPlayer(player);
        }
    }

    private void createInitialEvent(DBViewModel dbViewModel) {
        Utils.loadEventIntoDB(dbViewModel.getPlayersList(), dbViewModel, dbViewModel.getBallList(), new Action.WrongAction());
    }

    @Override
    public void onBackPressed() {
        mViewModel.deleteAllPlayers();
        super.onBackPressed();
    }
}
