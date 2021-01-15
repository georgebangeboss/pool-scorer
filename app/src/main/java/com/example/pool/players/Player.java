package com.example.pool.players;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.pool.Utils;
import com.example.pool.balls.Ball;
import com.example.pool.room.DBViewModel;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "player_table")
public class Player {
    @PrimaryKey
    private int playerIndex;

    private String playerTurn;

    private int playerPosition;

    private String playerName;

    private int playerScores;

    private String firstTouch;

    public String getFirstTouch() {
        return firstTouch;
    }

    public void setFirstTouch(String firstTouch) {
        this.firstTouch = firstTouch;
    }


    public Player(String playerName, int playerScores, int playerPosition, int playerIndex) {
        this.playerName = playerName;
        this.playerScores = playerScores;
        this.playerPosition = playerPosition;
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(String playerTurn) {
        this.playerTurn = playerTurn;
    }

    public int getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(int playerPosition) {
        this.playerPosition = playerPosition;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(int playerScores) {
        this.playerScores = playerScores;
    }

    public static List<Player> createInitialPlayers(List<String> playerNames) {
        List<Player> mPlayerNames = new ArrayList<>();
        Player player1 = new Player(playerNames.get(0).toUpperCase(), 0, 0, 1);
        player1.setPlayerTurn(Utils.MY_TURN);
        player1.setFirstTouch(Utils.FIRST_TOUCH_FALSE);
        mPlayerNames.add(player1);
        playerNames.remove(0);
        for (int i = 0; i < playerNames.size(); i++) {
            int index = i + 2;
            Player player = new Player(playerNames.get(i).toUpperCase(), 0, 0, index);
            player.setPlayerTurn(Utils.NOT_MY_TURN);
            player.setFirstTouch(Utils.FIRST_TOUCH_FALSE);
            mPlayerNames.add(player);
        }
        return mPlayerNames;
    }
    public boolean isKickedOutFromDB(DBViewModel dbViewModel) {
        List<Ball> ballList = dbViewModel.getBallList();
        List<Player> playerList = dbViewModel.getPlayersList();
        return isKickedOut(ballList,playerList);
    }
    public boolean isKickedOut(List<Ball> ballList,List<Player> playerList){
        int firstPlayerScore=playerList.get(0).getPlayerScores();
        int matSum = 0;
        for (Ball ball : ballList) {
            if (ball.getBallAt().hashCode() == Utils.ON_MAT.hashCode()) {
                matSum += ball.getBallValue();
            }
        }
        int highest = playerList.stream().map(i -> i.getPlayerScores()).reduce(firstPlayerScore, (c, e) -> (e > c) ? e : c);
        return ((this.getPlayerScores() + matSum) < highest);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj==null){
            return false;
        }
        if(obj==this){
            return true;
        }
        if(!(obj instanceof Player)){
            return false;
        }
        Player newPlayer=(Player)obj;
        if(newPlayer.getPlayerIndex()==this.getPlayerIndex()){
            return true;
        }else
            return false;
    }

    @Override
    public int hashCode() {
        int prime= 31;
        int result=prime*this.playerIndex;
        return result;
    }
}
