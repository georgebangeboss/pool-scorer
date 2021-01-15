package com.example.pool.gui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.pool.R;
import com.example.pool.room.DBViewModel;

public class WinnerPage extends CurrentPlayerScreen {
    private DBViewModel mViewModel;
    private Button mNewGame, mExit, mSaveGame;
    private RecyclerView mRecyclerView;
    private TextView mWinnerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String winStatus = getIntent().getStringExtra(CurrentPlayerScreen.EXTRA_WIN_STATUS);
        String winStatement = getIntent().getStringExtra(CurrentPlayerScreen.EXTRA_WIN_STATEMENT);
        mViewModel = new ViewModelProvider(this).get(DBViewModel.class);

        mNewGame = findViewById(R.id.new_game_btn);
        mExit = findViewById(R.id.exit_btn);
        mSaveGame = findViewById(R.id.save_game_btn);
        mRecyclerView = findViewById(R.id.player_results_recycler_view);
        mWinnerTextView = findViewById(R.id.winner_text_view);

        if (winStatus == CurrentPlayerScreen.YES_WINNER) {
            setContentView(R.layout.activity_winner_page_yes_winner);
            mWinnerTextView.setText(winStatement);
        } else if (winStatus == CurrentPlayerScreen.NO_WINNER) {
            setContentView(R.layout.activity_winner_page_no_winner);
            mWinnerTextView.setText(winStatement);
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.go_to_events:
                finish();
                gotoEvents(mViewModel);
                return true;
            case R.id.go_to_mat:
                finish();
                gotoMat(mViewModel);
                return true;
            case R.id.go_to_scores:
                finish();
                gotoScoreBoard(mViewModel);
                return true;
            case R.id.restart_game:
                finish();
                restartGameDialog(mViewModel);
                return true;
            case R.id.go_to_saved_games:
                //TODO
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
