package com.example.pool.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pool.Utils;
import com.example.pool.balls.Ball;
import com.example.pool.players.Player;

import java.util.List;

@Dao
public interface PlayerDao {
    @Delete
    void deletePlayer(Player player);

    @Update
    void updatePlayer(Player player);

    @Insert
    void insertPlayer(Player player);

    @Query("SELECT * FROM  player_table")
    LiveData<List<Player>> findAllPlayersLive();

    @Query("SELECT * FROM  player_table")
    List<Player> findAllPlayers();

    @Query("DELETE FROM player_table")
    void deleteAllPlayers();

    @Query("SELECT * FROM player_table WHERE playerTurn = 'MY_TURN' ")
    List<Player> findCurrentPlayer();


}
