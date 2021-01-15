package com.example.pool.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pool.R;
import com.example.pool.Utils;
import com.example.pool.balls.Ball;
import com.example.pool.room.DBViewModel;

import java.util.ArrayList;
import java.util.List;

public class MatKiss extends AppCompatActivity {
    private DBViewModel mViewModel;
    public static final String EXTRA_KISSED_BALL = "package com.example.pool.gui.EXTRA_KISSED_BALL";
    private List<Button> allButtons;
    public static Ball kissedBall = null;
    private static int[] buttonIDs = {R.id.ball_one_btn, R.id.ball_two_btn, R.id.ball_three_btn, R.id.ball_four_btn, R.id.ball_five_btn, R.id.ball_six_btn,
            R.id.ball_seven_btn, R.id.ball_eight_btn, R.id.ball_nine_btn, R.id.ball_ten_btn, R.id.ball_eleven_btn, R.id.ball_twelve_btn, R.id.ball_thirteen_btn,
            R.id.ball_fourteen_btn, R.id.ball_fifteen_btn, R.id.white_ball_btn};
    private Button next, clear;
    private TextView showKissedTextView;
    private static Button clickedButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat);
        setTitle("KISS");

        mViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        List<Ball> allBalls = mViewModel.getBallList();
        CurrentPlayerScreen.removeWhiteBall(allBalls);

        allButtons =initialiseButtons(buttonIDs);

        next = findViewById(R.id.ok_btn);
        clear = findViewById(R.id.clear);
        showKissedTextView = findViewById(R.id.show_potted_kissed_tv);
        prepareGui(allBalls);

        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;

                int touchedBallIndex = Utils.findIndexOfButton(btn, allButtons);
                Ball clickedBall = Utils.getBallAtIndex(touchedBallIndex, mViewModel);
                if (clickedBall.hashCode() == 0) {
                    Utils.toaster(MatKiss.this, "White Ball can't be kissed");
                    return;
                }
                if (clickedBall.isBallAvailable()) {
                    if (clickedButton != null) {
                        //reset a previously selected button to default colour if there's any
                        System.out.println("there's a clicked button");
                        clickedButton.setBackground(getDrawable(R.drawable.corner_curve_ball));
                        kissedBall = null;
                        clickedButton = null;

                    }
                    if (kissedBall == null) {
                        //select the button
                        kissedBall = clickedBall;
                        showKissedTextView.setText(kissedBall.getBallName());
                        btn.setBackground(getDrawable(R.drawable.corner_curve_ball_touched));
                        clickedButton = btn;
                    } else {
                        //deselect the button
                        kissedBall = null;
                        showKissedTextView.setText("");
                        btn.setBackground(getDrawable(R.drawable.corner_curve_ball));
                        clickedButton = null;
                    }

                } else {
                    //TODO setVibration
                    Utils.toaster(MatKiss.this, "Ball selected not on mat");
                }

            }
        };
        for (Button btn : allButtons) {
            btn.setOnClickListener(mOnClickListener);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBall();
                finish();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kissedBall = null;
                showKissedTextView.setText("");
                prepareGui(allBalls);
            }
        });

    }

    private void saveBall() {
        Intent intent = new Intent();
        if (kissedBall != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(EXTRA_KISSED_BALL, kissedBall);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            setResult(RESULT_CANCELED, intent);
            finish();
        }

    }

    private void prepareGui(List<Ball> allBalls) {
        for (Ball ball : allBalls) {
            Button btn = allButtons.get(ball.getBallIndex() - 1);
            if (!ball.isBallAvailable()) {
                btn.setBackground(getDrawable(R.drawable.corner_curve_ball_unavailable));
            } else {
                btn.setBackground(getDrawable(R.drawable.corner_curve_ball));
            }
        }
        allButtons.get(15).setBackground(getDrawable(R.drawable.corner_curve_ball_unavailable));
    }
    public List<Button> initialiseButtons(int[] buttonIDs){
        List<Button> allButtons=new ArrayList<>();
        allButtons.add(findViewById(buttonIDs[3]));
        allButtons.add(findViewById(buttonIDs[4]));
        allButtons.add(findViewById(buttonIDs[5]));
        allButtons.add(findViewById(buttonIDs[6]));
        allButtons.add(findViewById(buttonIDs[7]));
        allButtons.add(findViewById(buttonIDs[8]));
        allButtons.add(findViewById(buttonIDs[9]));
        allButtons.add(findViewById(buttonIDs[10]));
        allButtons.add(findViewById(buttonIDs[11]));
        allButtons.add(findViewById(buttonIDs[12]));
        allButtons.add(findViewById(buttonIDs[13]));
        allButtons.add(findViewById(buttonIDs[14]));
        allButtons.add(findViewById(buttonIDs[0]));
        allButtons.add(findViewById(buttonIDs[1]));
        allButtons.add(findViewById(buttonIDs[2]));
        allButtons.add(findViewById(buttonIDs[15]));
        return allButtons;
    }

}

