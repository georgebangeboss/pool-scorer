package com.example.pool.gui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.pool.R;
import com.example.pool.action.EventAction;
import com.example.pool.players.Player;
import com.example.pool.room.DBViewModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Splash extends AppCompatActivity {

    private static final String TAG = "Splash";
    private DBViewModel mViewModel;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_splash);
        mViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        Log.d(TAG, "onCreate: view model created successful!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Timer timer = new Timer();
        List<EventAction> eventActions = mViewModel.getEventList();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (eventActions.isEmpty()) {
                    Intent intent = new Intent(Splash.this, InputScreen.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(Splash.this, CurrentPlayerScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);

    }

}
