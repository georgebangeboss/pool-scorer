package com.example.pool.gui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pool.R;
import com.example.pool.Utils;
import com.example.pool.balls.Ball;
import com.example.pool.room.DBViewModel;

import java.util.ArrayList;
import java.util.List;

public class MatFragment extends Fragment {

    private DBViewModel mViewModel;
    private List<Button> allButtons;
    private static int[] buttonIDs = {R.id.ball_one, R.id.ball_two, R.id.ball_three, R.id.ball_four, R.id.ball_five, R.id.ball_six,
            R.id.ball_seven, R.id.ball_eight, R.id.ball_nine, R.id.ball_ten, R.id.ball_eleven, R.id.ball_twelve, R.id.ball_thirteen,
            R.id.ball_fourteen, R.id.ball_fifteen, R.id.ball_white};

    public static MatFragment newInstance(DBViewModel dbViewModel) {
        MatFragment fragment = new MatFragment();
        fragment.mViewModel = dbViewModel;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mat, container, false);

        allButtons = initializeButtons(v, buttonIDs);
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button btn = (Button) v;

                int touchedBallIndex = Utils.findIndexOfButton(btn, allButtons);
                Ball clickedBall = Utils.getBallAtIndex(touchedBallIndex, mViewModel);
                if(clickedBall.isBallAvailable()){
                    Utils.toaster(getContext(),clickedBall.getBallName()+" is on MAT");
                }else{
                    Utils.toaster(getContext(),clickedBall.getBallName()+" is in POT");
                }
            }
        };
        for (Button btn : allButtons) {
            btn.setOnClickListener(mOnClickListener);
        }
        prepareGui(mViewModel.getBallList());

        mViewModel.getTheBallsLive().observe(getActivity(), new Observer<List<Ball>>() {
            @Override
            public void onChanged(List<Ball> balls) {
                prepareGui(balls);
            }
        });
        return v;
    }
    public void prepareGui(List<Ball> allBalls) {
        Ball currentBall=mViewModel.findCurrentBall();
        for (Ball ball : allBalls) {
            Button btn=allButtons.get(ball.getBallIndex() - 1);
            if(ball.equals(currentBall)){
                //to update gui
                btn.setBackground(getActivity().getDrawable(R.drawable.corner_curve_ball_touched));
            }
            if (!ball.isBallAvailable()) {
                btn.setBackground(getActivity().getDrawable(R.drawable.corner_curve_ball_unavailable));
            }else {
                btn.setBackground(getActivity().getDrawable(R.drawable.corner_curve_ball));
            }
        }
        allButtons.get(15).setBackground(getActivity().getDrawable(R.drawable.corner_curve_ball));
    }

    public List<Button> initializeButtons(View v, int[] buttonIDs) {
        List<Button> allButtons = new ArrayList<>();
        allButtons.add(v.findViewById(buttonIDs[3]));
        allButtons.add(v.findViewById(buttonIDs[4]));
        allButtons.add(v.findViewById(buttonIDs[5]));
        allButtons.add(v.findViewById(buttonIDs[6]));
        allButtons.add(v.findViewById(buttonIDs[7]));
        allButtons.add(v.findViewById(buttonIDs[8]));
        allButtons.add(v.findViewById(buttonIDs[9]));
        allButtons.add(v.findViewById(buttonIDs[10]));
        allButtons.add(v.findViewById(buttonIDs[11]));
        allButtons.add(v.findViewById(buttonIDs[12]));
        allButtons.add(v.findViewById(buttonIDs[13]));
        allButtons.add(v.findViewById(buttonIDs[14]));
        allButtons.add(v.findViewById(buttonIDs[0]));
        allButtons.add(v.findViewById(buttonIDs[1]));
        allButtons.add(v.findViewById(buttonIDs[2]));
        allButtons.add(v.findViewById(buttonIDs[15]));
        return allButtons;
    }
}
