package com.example.pool.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.pool.R;
import com.example.pool.Utils;
import com.example.pool.room.DBViewModel;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.gson.reflect.TypeToken.get;


public class InputScreen extends AppCompatActivity {
    public static final String PLAYER_NAMES = "com.example.pool.gui.PLAYER_NAMES";
    private static final String TAG = "InputScreen";
    private List<EditText> playerList;
    private DBViewModel mViewModel;
    private ImageButton goToPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_input);

        playerList = playersById(R.id.player1, R.id.player2, R.id.player3, R.id.player4,
                R.id.player5, R.id.player6, R.id.player7, R.id.player8);

        goToPreview = findViewById(R.id.go_to_preview);
        mViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        goToPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> playerNames = preparePlayerNames(playerList);
                if (isRepeated(playerNames)) {
                    Utils.toaster(InputScreen.this, "Some names have been repeated");
                } else {
                    if (playerNames.isEmpty()) {
                        Utils.toaster(InputScreen.this, "Please enter players");
                    } else {
                        if (playerNames.size() == 1) {
                            Utils.toaster(InputScreen.this, "can't have one player");
                        } else {
                            Intent intent = new Intent(InputScreen.this, Preview.class);
                            intent.putStringArrayListExtra(PLAYER_NAMES, playerNames);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    private boolean isRepeated(List<String> playerNames) {
        ArrayList<String> names = new ArrayList<>();
        Stream<String> s = names.stream();
        Map<String, Long> m = s.collect(Collectors.groupingBy(Function.identity(), Collectors.<String>counting()));
        List<Long> repeated = m.values().stream().filter(i -> i > 1).collect(Collectors.toList());
        return !repeated.isEmpty();
    }

    private List<EditText> playersById(int... ids) {
        ArrayList<EditText> list = new ArrayList<>();
        for (int id : ids) {
            list.add((EditText) findViewById(id));
        }
        return ImmutableList.copyOf(list);
    }

    private ArrayList<String> preparePlayerNames(List<EditText> editTexts) {
        ArrayList<String> playerNames = new ArrayList<>();
        for (EditText editText : editTexts) {
            String text = editText.getText().toString().trim().toUpperCase();
            if (text.length() > 0) {
                playerNames.add(text);
            }
        }
        return playerNames;
    }
}
