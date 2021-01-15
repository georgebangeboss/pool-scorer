package com.example.pool.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pool.action.EventAction;
import com.example.pool.balls.Ball;
import com.example.pool.players.Player;

import java.util.List;

public class DBViewModel extends AndroidViewModel {

    private DBRepository mRepository;
    private LiveData<List<Ball>> theBalls;
    private LiveData<List<Player>> thePlayers;
    private LiveData<List<EventAction>> theEvents;

    public DBViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DBRepository(application);
        theBalls = mRepository.getTheBallsLive();
        thePlayers = mRepository.getThePlayersLive();
        theEvents=mRepository.getTheEventsLive();
    }

    public void updateBall(Ball ball) {
        mRepository.updateBall(ball);
    }

    public void updatePlayer(Player player) {
        mRepository.updatePlayer(player);
    }

    public void updateEvent(EventAction event){
        mRepository.updateEvent(event);
    }

    public void deleteBall(Ball ball) {
        mRepository.deleteBall(ball);
    }

    public void deletePlayer(Player player) {
        mRepository.deletePlayer(player);
    }

    public void deleteEvent(EventAction event){
        mRepository.deleteEvent(event);
    }

    public void insertBall(Ball ball) {
        mRepository.insertBall(ball);
    }

    public void insertPlayer(Player player) {
        mRepository.insertPlayer(player);
    }

    public void insertEvent(EventAction event){
        mRepository.insertEvent(event);
    }

    public void deleteAllBalls() {
        mRepository.deleteAllBalls();
    }

    public void deleteAllPlayers() {
        mRepository.deleteAllPlayers();
    }

    public void deleteAllEvents(){
        mRepository.deleteAllEvents();
    }


    public LiveData<List<Ball>> getTheBallsLive() {
        return theBalls;
    }

    public LiveData<List<EventAction>> getTheEventsLive() {
        return theEvents;
    }

    public LiveData<List<Player>> getThePlayersLive() {
        return thePlayers;
    }

    public List<Player> getPlayersList() {
        return mRepository.getPlayerList();
    }

    public List<Ball> getBallList() {
        return mRepository.getBallList();
    }

    public List<EventAction> getEventList(){
        return mRepository.getEventsList();
    }

    public Ball findCurrentBall() {
        return mRepository.findCurrentBall();
    }

    public Player findCurrentPlayer() {
        return mRepository.findCurrentPlayer();
    }
}
