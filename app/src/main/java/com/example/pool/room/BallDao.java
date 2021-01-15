package com.example.pool.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pool.Utils;
import com.example.pool.balls.Ball;

import java.util.List;

@Dao
public interface BallDao {
    @Delete
    void deleteBall(Ball ball);

    @Update
    void updateBall(Ball ball);

    @Insert
    void insertBall(Ball ball);

    @Query("SELECT * FROM  balls_table ORDER BY ballIndex ASC")
    LiveData<List<Ball>> findAllBallsLive();

    @Query("SELECT * FROM  balls_table ORDER BY ballIndex ASC")
    List<Ball> findAllBalls();

    @Query("DELETE FROM balls_table")
    void deleteAllBalls();

    @Query("SELECT * FROM balls_table WHERE ballStatus= 'ACTIVE'")
    List<Ball> findCurrentBall();
}
