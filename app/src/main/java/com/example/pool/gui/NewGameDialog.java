package com.example.pool.gui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.pool.R;
import com.example.pool.balls.Ball;
import com.example.pool.room.DBViewModel;

public class NewGameDialog extends AppCompatDialogFragment {
    private DBViewModel mViewModel;

    public NewGameDialog(DBViewModel dbViewModel) {
        mViewModel = dbViewModel;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.yes_no_dialog_fragment,null);
        builder.setView(v)
                .setTitle("New Game?")
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO delete events, new balls, clear players
                        //TODO kill activities and open Input Screen
                    }
                });
        return builder.create();
    }

    private void deleteEvents() {
        mViewModel.deleteAllEvents();
    }

    private void newBalls() {
        mViewModel.deleteAllBalls();
        for(Ball ball:Ball.createInitialBalls()){
            mViewModel.insertBall(ball);
        }
    }

    private void clearPlayers() {
        mViewModel.deleteAllPlayers();
    }
}
