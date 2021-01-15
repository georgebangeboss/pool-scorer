package com.example.pool.action;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.pool.balls.Ball;
import com.example.pool.players.Player;

import java.util.List;

@Entity(tableName = "events_table")
public class EventAction {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String actionString;

    private String cachedBallString;

    private String cachedPlayerString;

    public EventAction(String actionString, String cachedBallString, String cachedPlayerString) {
        this.actionString = actionString;
        this.cachedBallString = cachedBallString;
        this.cachedPlayerString = cachedPlayerString;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getActionString() {
        return actionString;
    }

    public String getCachedBallString() {
        return cachedBallString;
    }

    public String getCachedPlayerString() {
        return cachedPlayerString;
    }
}
