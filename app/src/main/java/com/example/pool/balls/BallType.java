package com.example.pool.balls;

import com.example.pool.Utils;

enum BallType {
    ball_4(1, "ball4 ", 6, Utils.ON_MAT,Utils.BALL_IS_ACTIVE),
    ball_5(2, "ball5 ", 6, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_6(3, "ball6 ", 6, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_7(4, "ball7 ", 7, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_8(5, "ball8 ", 8, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_9(6, "ball9 ", 9, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_10(7, "ball10", 10, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_11(8, "ball11", 11, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_12(9, "ball12", 12, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_13(10, "ball13", 13, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_14(11, "ball14", 14, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_15(12, "ball15", 15, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_1(13, "ball1", 16, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_2(14, "ball2", 17, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_3(15, "ball3 ", 18, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE),
    ball_0(16, "ball0", 0, Utils.ON_MAT,Utils.BALL_NOT_ACTIVE)
    ;
    private int ballValue;
    private String ballName;
    private int ballIndex;
    private String ballAt;
    private String ballStatus;

    BallType(int ballIndex, String ballName, int ballValue, String ballAt,String ballStatus) {
        this.ballValue = ballValue;
        this.ballAt = ballAt;
        this.ballIndex = ballIndex;
        this.ballName = ballName;
        this.ballStatus=ballStatus;
    }

    public String getBallAt() {
        return ballAt;
    }

    public String getBallStatus() {
        return ballStatus;
    }

    public int getBallValue() {
        return ballValue;
    }

    public String getBallName() {
        return ballName;
    }

    public int getBallIndex() {
        return ballIndex;
    }
}
