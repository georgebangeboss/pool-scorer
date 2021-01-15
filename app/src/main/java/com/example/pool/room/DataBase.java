package com.example.pool.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.pool.action.EventAction;
import com.example.pool.balls.Ball;
import com.example.pool.players.Player;

@Database(entities = {Ball.class, Player.class, EventAction.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public static DataBase dataBaseInstance;
    private static RoomDatabase.Callback mCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new InitialBallsLoadAsyncTask(dataBaseInstance).execute();
        }
    };

    public abstract BallDao ballDao();

    public abstract EventDao eventsDao();

    public abstract PlayerDao playersDao();

    public static synchronized DataBase getDataBaseInstance(Context context) {
        if (dataBaseInstance == null) {
            dataBaseInstance = Room.databaseBuilder(context.getApplicationContext(), DataBase.class, "d_base")
                    .fallbackToDestructiveMigration()
                    .addCallback(mCallBack)
                    .build();
        }
        return dataBaseInstance;
    }

    private static class InitialBallsLoadAsyncTask extends AsyncTask<Void,Void,Void>{
        private BallDao ballDao;
        private InitialBallsLoadAsyncTask(DataBase dataBase){
            this.ballDao=dataBase.ballDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            for(Ball ball:Ball.createInitialBalls()){
                ballDao.insertBall(ball);
            }
            return null;
        }
    }

}
