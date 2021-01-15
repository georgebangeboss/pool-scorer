package com.example.pool.balls;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.pool.Utils;


import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "balls_table")
public class Ball implements Parcelable {
    @PrimaryKey
    private int ballIndex;

    private int ballValue;
    private String ballName;
    private String ballAt;
    private String ballStatus;

    public Ball(String ballName, int ballValue, int ballIndex, String ballAt, String ballStatus) {
        this.ballValue = ballValue;
        this.ballName = ballName;
        this.ballIndex = ballIndex;
        this.ballAt = ballAt;
        this.ballStatus = ballStatus;
    }

    protected Ball(Parcel in) {
        ballIndex = in.readInt();
        ballValue = in.readInt();
        ballName = in.readString();
        ballAt = in.readString();
        ballStatus = in.readString();
    }

    public static final Creator<Ball> CREATOR = new Creator<Ball>() {
        @Override
        public Ball createFromParcel(Parcel in) {
            return new Ball(in);
        }

        @Override
        public Ball[] newArray(int size) {
            return new Ball[size];
        }
    };

    public String getBallAt() {
        return ballAt;
    }

    public void setBallNotActive() {
        this.ballStatus = Utils.BALL_NOT_ACTIVE;
    }

    public void setBallActive() {
        this.ballStatus = Utils.BALL_IS_ACTIVE;
    }

    public void setBallInPot() {
        this.ballAt = Utils.IN_POT;
    }

    public void setBallOnMat() {
        this.ballAt = Utils.ON_MAT;
    }

    public String getBallStatus() {
        return ballStatus;
    }

    public int getBallIndex() {
        return ballIndex;
    }

    public int getBallValue() {
        return ballValue;
    }

    public String getBallName() {
        return ballName;
    }


    public int hashCode() {
        int result;
        result = getBallValue() * 31 * getBallIndex() * getBallName().hashCode();
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Ball)) {
            return false;
        }
        Ball ball = (Ball) obj;
        if (ball.getBallValue() == this.getBallValue()
                && ball.getBallIndex() == this.getBallIndex()
                && ball.getBallName().hashCode() == this.getBallName().hashCode()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBallAvailable() {
        return this.getBallAt().hashCode() == Utils.ON_MAT.hashCode();
    }

    public static List<Ball> createInitialBalls() {
        List<Ball> createdBalls = new ArrayList<>();
        for (BallType ballType : BallType.values()) {
            createdBalls.add(new Ball(ballType.getBallName(), ballType.getBallValue(),
                    ballType.getBallIndex(), ballType.getBallAt(), ballType.getBallStatus()));
        }
        return createdBalls;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ballIndex);
        dest.writeInt(ballValue);
        dest.writeString(ballName);
        dest.writeString(ballAt);
        dest.writeString(ballStatus);
    }

}
