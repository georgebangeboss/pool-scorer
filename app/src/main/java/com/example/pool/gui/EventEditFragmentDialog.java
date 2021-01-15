package com.example.pool.gui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.pool.R;
import com.example.pool.Utils;
import com.example.pool.action.EventAction;
import com.example.pool.balls.Ball;
import com.example.pool.players.Player;
import com.example.pool.room.DBViewModel;
import com.google.gson.Gson;

import java.util.List;

public class EventEditFragmentDialog extends AppCompatDialogFragment {
    private final int position;
    private Button buttonOk;
    private Button buttonCancel;
    private TextView prompt;
    private EventAction eventAction;
    private DBViewModel mViewModel;
    private EventDialogListener listener;

    public interface EventDialogListener{
        void updateCurrentPlayerUI(DBViewModel dbViewModel);
        void removeEventsFragment();
        void makeFragmentUIDisappear();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        try {
            listener=(EventDialogListener)context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public EventEditFragmentDialog(EventAction eventAction, int position) {
        this.eventAction = eventAction;
        this.position = position;
    }


    @NonNull
    @Override

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.edit_event_fragment_proceed, null);
        builder.setView(v)
                .setTitle("Edit Event?");
        buttonCancel = v.findViewById(R.id.button_cancel_prompt);
        buttonOk = v.findViewById(R.id.button_ok_prompt);
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete all following events but that
                deleteEvents(mViewModel);
                EventAction lastEvent = getLastEvent(mViewModel);
                String cachedBallListString = lastEvent.getCachedBallString();
                String cachedPlayerListString = lastEvent.getCachedPlayerString();

                //delete the last Event
                mViewModel.deleteEvent(lastEvent);

                List<Ball> ballListToLoad = Utils.deserializeBalls(cachedBallListString, new Gson());
                List<Player> playerListToLoad = Utils.deserializePlayers(cachedPlayerListString, new Gson());

                updateToDB(ballListToLoad, playerListToLoad);
                //Updates CurrentPlayerScoreUI
                dismiss();
                listener.updateCurrentPlayerUI(mViewModel);
                listener.removeEventsFragment();
                listener.makeFragmentUIDisappear();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        prompt = v.findViewById(R.id.event_edit_prompt);
        prompt.setText(R.string.edit_event);
        return builder.create();
    }

    private void updateToDB(List<Ball> ballList, List<Player> playerList) {
        for (Player player : playerList) {
            mViewModel.updatePlayer(player);
        }
        for (Ball ball : ballList) {
            mViewModel.updateBall(ball);
        }
    }

    private EventAction getLastEvent(DBViewModel dbViewModel) {
        List<EventAction> events = dbViewModel.getEventList();
        return events.get(events.size() - 1);
    }

    private void deleteEvents(DBViewModel dbViewModel) {
        List<EventAction> events = dbViewModel.getEventList();
        for (int i = 0; i < events.size(); i++) {
            if ( i > position) {
                mViewModel.deleteEvent(events.get(i));
            }
        }
    }

}
