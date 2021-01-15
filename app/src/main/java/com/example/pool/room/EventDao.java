package com.example.pool.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pool.action.EventAction;

import java.util.List;

@Dao
public interface EventDao {
    @Delete
    void delete(EventAction event);

    @Insert
    void insert(EventAction event);

    @Update
    void update(EventAction event);

    @Query("DELETE FROM events_table")
    void deleteAllEvents();

    @Query("SELECT * FROM events_table")
    List<EventAction>  findAllEvents();

    @Query("SELECT * FROM events_table")
    LiveData<List<EventAction>> findAllEventsLive();
}
