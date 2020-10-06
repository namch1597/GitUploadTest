package com.example.task.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MetroDAO {

    @Query("SELECT * FROM metro")
    List<Metro> getAll();

    @Query("SELECT * FROM metro where name = :name LIMIT 1")
    Metro getDetailPath(String name);

    //이미 저장되어 있는 경우 데이터를 덮어씀
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMetro(Metro metro);

}
