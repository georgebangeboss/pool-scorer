package com.example.pool.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.pool.action.EventAction;
import com.example.pool.balls.Ball;
import com.example.pool.players.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DBRepository {
    private BallDao ballDao;
    private PlayerDao playerDao;
    private EventDao eventDao;
    private LiveData<List<Ball>> theBalls;
    private LiveData<List<Player>> thePlayers;
    private LiveData<List<EventAction>> theEvents;

    public DBRepository(Application application) {
        DataBase dataBase = DataBase.getDataBaseInstance(application);
        playerDao = dataBase.playersDao();
        ballDao = dataBase.ballDao();
        eventDao =dataBase.eventsDao();
        theBalls = ballDao.findAllBallsLive();
        thePlayers = playerDao.findAllPlayersLive();
        theEvents= eventDao.findAllEventsLive();
    }

    public List<Player> getPlayerList(){
        List<Player> allPlayers=new ArrayList<>();
        try {
            allPlayers= new PlayerListAsyncTask(playerDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allPlayers;
    }
    public List<Ball> getBallList(){
        List<Ball> allBalls = new ArrayList<>();
        try{
            allBalls=new BallListAsyncTask(ballDao).execute().get();
        }catch(Exception e){
            e.printStackTrace();
        }
        return allBalls;
    }
    public List<EventAction> getEventsList(){
        List<EventAction> allEvents=new ArrayList<>();
        try {
            allEvents=new EventsListAsyncTask(eventDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allEvents;
    }

    public LiveData<List<Ball>> getTheBallsLive() {
        return theBalls;
    }

    public LiveData<List<Player>> getThePlayersLive() {
        return thePlayers;
    }

    public LiveData<List<EventAction>> getTheEventsLive(){return theEvents;}

    public void insertBall(Ball ball) {
        new InsertBallAsyncTask(ballDao).execute(ball);
    }

    public void insertPlayer(Player player) {
        new InsertPlayerAsyncTask(playerDao).execute(player);
    }
    public void insertEvent(EventAction event){
        new InsertEventAsyncTask(eventDao).execute(event);
    }
    public Player findCurrentPlayer(){
        Player player=null;
        try {
            player = new FindCurrentPlayerAsyncTask(playerDao).execute().get().get(0);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  player;
    }
    public Ball findCurrentBall(){
        Ball ball=null;
        try {
            ball = new FindCurrentBallAsyncTask(ballDao).execute().get().get(0);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ball;
    }

    public void deleteBall(Ball ball) {
        new DeleteBallAsyncTask(ballDao).execute(ball);
    }

    public void deletePlayer(Player player) {
        new DeletePlayerAsyncTask(playerDao).execute(player);
    }
    public void deleteEvent(EventAction event){
        new DeleteEventAsyncTask(eventDao).execute(event);
    }

    public void deleteAllBalls() {
        new DeleteAllBallsAsyncTask(ballDao).execute();
    }

    public void deleteAllPlayers() {
        new DeleteAllPlayersAsyncTask(playerDao).execute();
    }

    public void deleteAllEvents(){
        new DeleteAllEventsAsyncTask(eventDao).execute();
    }

    public void updateBall(Ball ball) {
        new UpdateBallAsyncTask(ballDao).execute(ball);
    }

    public void updatePlayer(Player player) {
        new UpdatePlayerAsyncTask(playerDao).execute(player);
    }

    public void updateEvent(EventAction event){
        new UpdateEventAsyncTask(eventDao).execute(event);
    }
    private static class BallListAsyncTask extends AsyncTask<Void,Void,List<Ball>>{
        private BallDao ballDao;
        private BallListAsyncTask(BallDao ballDao){
            this.ballDao=ballDao;
        }

        @Override
        protected List<Ball> doInBackground(Void... voids) {
            List<Ball> allBalls = ballDao.findAllBalls();
            return allBalls;
        }

        @Override
        protected void onPostExecute(List<Ball> balls) {
            super.onPostExecute(balls);

        }
    }
    private static class PlayerListAsyncTask extends AsyncTask<Void,Void,List<Player>>{
        private PlayerDao playerDao;
        private PlayerListAsyncTask(PlayerDao playerDao){
            this.playerDao=playerDao;
        }

        @Override
        protected List<Player> doInBackground(Void... voids) {
            List<Player> allPlayers= playerDao.findAllPlayers();
            return allPlayers;
        }

        @Override
        protected void onPostExecute(List<Player> players) {
            super.onPostExecute(players);
        }
    }
    private static  class EventsListAsyncTask extends  AsyncTask<Void,Void,List<EventAction>>{
        private EventDao eventDao;
        private EventsListAsyncTask(EventDao eventDao){
            this.eventDao=eventDao;
        }

        @Override
        protected List<EventAction> doInBackground(Void... voids) {
            return eventDao.findAllEvents();
        }
    }
    private static class FindCurrentPlayerAsyncTask extends AsyncTask<Void,Void,List<Player>>{
        private PlayerDao playerDao;
        private FindCurrentPlayerAsyncTask(PlayerDao playerDao){
            this.playerDao=playerDao;
        }

        @Override
        protected List<Player> doInBackground(Void... voids) {
            List<Player> allPlayers= playerDao.findCurrentPlayer();
            return allPlayers;
        }

    }
    private static class FindCurrentBallAsyncTask extends AsyncTask<Void,Void,List<Ball>>{
        private BallDao ballDao;
        private FindCurrentBallAsyncTask(BallDao ballDao){
            this.ballDao=ballDao;
        }

        @Override
        protected List<Ball> doInBackground(Void... voids) {
            List<Ball> allBalls= ballDao.findCurrentBall();
            return allBalls;
        }

    }

    private static class InsertBallAsyncTask extends AsyncTask<Ball, Void, Void> {
        private BallDao ballDao;

        private InsertBallAsyncTask(BallDao ballDao) {
            this.ballDao = ballDao;
        }

        @Override
        protected Void doInBackground(Ball... balls) {
            ballDao.insertBall(balls[0]);
            return null;
        }
    }

    private static class InsertPlayerAsyncTask extends AsyncTask<Player, Void, Void> {
        private PlayerDao playerDao;

        public InsertPlayerAsyncTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }

        @Override
        protected Void doInBackground(Player... players) {
            playerDao.insertPlayer(players[0]);
            return null;
        }
    }
    private static class InsertEventAsyncTask extends AsyncTask<EventAction,Void,Void>{
        private EventDao eventDao;
        public InsertEventAsyncTask(EventDao eventDao) {
            this.eventDao=eventDao;
        }

        @Override
        protected Void doInBackground(EventAction... eventActions) {
            eventDao.insert(eventActions[0]);
            return null;
        }
    }

    private static class DeleteBallAsyncTask extends AsyncTask<Ball, Void, Void> {
        private BallDao ballDao;

        private DeleteBallAsyncTask(BallDao ballDao) {
            this.ballDao = ballDao;
        }

        @Override
        protected Void doInBackground(Ball... balls) {
            ballDao.deleteBall(balls[0]);
            return null;
        }
    }
    private static class DeleteEventAsyncTask extends AsyncTask<EventAction,Void,Void>{
        private EventDao eventDao;
        public DeleteEventAsyncTask(EventDao eventDao) {
            this.eventDao=eventDao;
        }

        @Override
        protected Void doInBackground(EventAction... eventActions) {
            eventDao.delete(eventActions[0]);
            return null;
        }
    }

    private static class DeletePlayerAsyncTask extends AsyncTask<Player, Void, Void> {
        private PlayerDao playerDao;

        public DeletePlayerAsyncTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }

        @Override
        protected Void doInBackground(Player... players) {
            playerDao.deletePlayer(players[0]);
            return null;
        }
    }

    private static class UpdateBallAsyncTask extends AsyncTask<Ball, Void, Void> {
        private BallDao ballDao;

        private UpdateBallAsyncTask(BallDao ballDao) {
            this.ballDao = ballDao;
        }

        @Override
        protected Void doInBackground(Ball... balls) {
            ballDao.updateBall(balls[0]);
            return null;
        }
    }

    private static class UpdatePlayerAsyncTask extends AsyncTask<Player, Void, Void> {
        private PlayerDao playerDao;

        public UpdatePlayerAsyncTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }

        @Override
        protected Void doInBackground(Player... players) {
            playerDao.updatePlayer(players[0]);
            return null;
        }
    }
    private static class UpdateEventAsyncTask extends AsyncTask<EventAction, Void, Void> {
        private EventDao eventDao;

        public UpdateEventAsyncTask(EventDao eventDao) {
            this.eventDao=eventDao;
        }


        @Override
        protected Void doInBackground(EventAction... eventActions) {
            eventDao.update(eventActions[0]);
            return null;
        }
    }


    private static class DeleteAllBallsAsyncTask extends AsyncTask<Void, Void, Void> {
        private BallDao ballDao;

        private DeleteAllBallsAsyncTask(BallDao ballDao) {
            this.ballDao = ballDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ballDao.deleteAllBalls();
            return null;
        }
    }
    private static class DeleteAllEventsAsyncTask extends AsyncTask<Void,Void,Void>{
        private EventDao eventDao;
        public DeleteAllEventsAsyncTask(EventDao eventDao) {
            this.eventDao=eventDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            eventDao.deleteAllEvents();
            return null;
        }
    }

    private static class DeleteAllPlayersAsyncTask extends AsyncTask<Void, Void, Void> {
        private PlayerDao playerDao;

        public DeleteAllPlayersAsyncTask(PlayerDao playerDao) {
            this.playerDao = playerDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            playerDao.deleteAllPlayers();
            return null;
        }
    }

}
