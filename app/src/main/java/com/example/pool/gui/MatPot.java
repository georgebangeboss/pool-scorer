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

public class MatPot extends AppCompatActivity {
    private DBViewModel mViewModel;
    public static final String EXTRA_CLICKED_BALLS = "package com.example.pool.gui.EXTRA_CLICKED_BALLS";
    private List<Button> allButtons;
    private ArrayList<Ball> clickedBalls = new ArrayList<Ball>();
    private static int[] buttonIDs = {R.id.ball_one_btn, R.id.ball_two_btn, R.id.ball_three_btn, R.id.ball_four_btn, R.id.ball_five_btn, R.id.ball_six_btn,
            R.id.ball_seven_btn, R.id.ball_eight_btn, R.id.ball_nine_btn, R.id.ball_ten_btn, R.id.ball_eleven_btn, R.id.ball_twelve_btn, R.id.ball_thirteen_btn,
            R.id.ball_fourteen_btn, R.id.ball_fifteen_btn, R.id.white_ball_btn};
    private Button next, clear;
    private TextView showPottedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat);
        setTitle("POT");
        mViewModel = new ViewModelProvider(this).get(DBViewModel.class);
        final List<Ball> allBalls=mViewModel.getBallList();

        allButtons=new MatKiss().initialiseButtons(buttonIDs);


        next = findViewById(R.id.ok_btn);
        clear = findViewById(R.id.clear);
        showPottedTextView = findViewById(R.id.show_potted_kissed_tv);
        prepareGui(allBalls);



        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;

                int touchedBallIndex = Utils.findIndexOfButton(btn, allButtons);
                Ball clickedBall = Utils.getBallAtIndex(touchedBallIndex, mViewModel);
                if (clickedBall.isBallAvailable() && !clickedBalls.contains(clickedBall)) {
                    //select ball when the button has been clicked for the first time
                    clickedBalls.add(clickedBall);
                    String displayString = toDisplay(clickedBalls);
                    showPottedTextView.setText(displayString);
                    btn.setBackground(getDrawable(R.drawable.corner_curve_ball_touched));

                } else if (clickedBall.isBallAvailable() && clickedBalls.contains(clickedBall)) {
                    //deselect ball when button has been clicked for the second time
                    clickedBalls.remove(clickedBall);
                    String displayString = toDisplay(clickedBalls);
                    showPottedTextView.setText(displayString);
                    btn.setBackground(getDrawable(R.drawable.corner_curve_ball));
                } else {
                    //TODO setVibration
                    Utils.toaster(MatPot.this, "Ball already in pot");
                }
            }
        };
        for (Button btn : allButtons) {
            btn.setOnClickListener(mOnClickListener);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBalls();
                finish();
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedBalls.clear();
                String displayString = toDisplay(clickedBalls);
                showPottedTextView.setText(displayString);
                prepareGui(allBalls);
            }
        });

    }

    private void prepareGui(List<Ball> allBalls) {
        for (Ball ball : allBalls) {
            Button btn=allButtons.get(ball.getBallIndex() - 1);
            if (!ball.isBallAvailable()) {
                btn.setBackground(getDrawable(R.drawable.corner_curve_ball_unavailable));
            }else {
                btn.setBackground(getDrawable(R.drawable.corner_curve_ball));
            }
        }
        allButtons.get(15).setBackground(getDrawable(R.drawable.corner_curve_ball));
    }

    private String toDisplay(List<Ball> clickedBalls) {
        String display = "";
        for (Ball ball : clickedBalls) {
            display += ball.getBallName();
            display += "  ";
        }
        return display;
    }


    private void saveBalls() {
        Intent intent = new Intent();
        if (!clickedBalls.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(EXTRA_CLICKED_BALLS, clickedBalls);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }else{
            setResult(RESULT_CANCELED,intent);
            finish();
        }

    }
    private boolean hasBall(Ball clickedBall,ArrayList<Ball> clickedBalls){
        for(Ball ball:clickedBalls){
            System.out.println(ball.equals(clickedBall));
            if(ball.equals(clickedBall)){
                return true;
            }
        }
        return false;
    }

}
