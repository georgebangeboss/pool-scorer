package com.example.pool;

import android.content.Context;
import android.widget.Button;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.example.pool.action.Action;
import com.example.pool.action.EventAction;
import com.example.pool.balls.Ball;
import com.example.pool.players.Player;
import com.example.pool.room.DBViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BALL_IS_ACTIVE = "ACTIVE";
    public static final String BALL_NOT_ACTIVE = "INACTIVE";
    public static final String ON_MAT = "MAT";
    public static final String IN_POT = "POT";
    public static final String MY_TURN = "MY_TURN";
    public static final String NOT_MY_TURN = "NOT_MY_TURN";
    public static final String FIRST_TOUCH_FALSE = "FALSE";
    public static final String FIRST_TOUCH_TRUE = "TRUE";

    public static int findIndexOfButton(Button btn, List<Button> allButtons) {
        for (int i = 0; i < allButtons.size(); i++) {
            if (btn.getId() == allButtons.get(i).getId()) {
                return i;
            }
        }
        throw new RuntimeException("the button don't exist");
    }

    public static void toaster(Context context, String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    public static Ball getBallAtIndex(int touchedBallIndex, DBViewModel dbViewModel) {
        List<Ball> ballList = dbViewModel.getBallList();
        return ballList.get(touchedBallIndex);
    }

    public static List<Ball> deserializeBalls(String storedBallsString, Gson gson) {
        Type listType = new TypeToken<ArrayList<Ball>>() {
        }.getType();
        List<Ball> theBalls = gson.fromJson(storedBallsString, listType);
        return theBalls;
    }

    public static List<Player> deserializePlayers(String storedPlayerString, Gson gson) {
        return gson.fromJson(storedPlayerString, new TypeToken<ArrayList<Player>>() {
        }.getType());
    }

    public static String serializePlayers(List<Player> playersToBeStored, Gson gson) {
        return gson.toJson(playersToBeStored);
    }

    public static String serializeBalls(List<Ball> ballsToBeStored, Gson gson) {
        return gson.toJson(ballsToBeStored);
    }

    public static void loadEventIntoDB(List<Player> cachedPlayerList, DBViewModel dbViewModel, List<Ball> cachedBallList, Action currentAction) {
        Gson gson = new Gson();
        dbViewModel.insertEvent(new EventAction(currentAction.toPrint() + " " + Action.kickOutStatement(dbViewModel, cachedBallList, cachedPlayerList), Utils.serializeBalls(cachedBallList, gson), Utils.serializePlayers(cachedPlayerList, gson)));
    }
}
