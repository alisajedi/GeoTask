package com.geotask.myapplication.Controllers.LocalFilesOps;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import com.geotask.myapplication.DataClasses.Task;

import java.util.List;


/**
 * Data Access Object for Task and local SQL database
 */
@Dao
public interface TaskDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Task task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMultiple(Task... tasks);

    @Query("SELECT * FROM tasks WHERE requesterId LIKE :requesterID")
    List<Task> selectByRequester(String requesterID);

    @Query("SELECT * FROM tasks")
    List<Task> selectAll();

    @Query("SELECT * FROM tasks WHERE status LIKE :status")
    List<Task> selectByStatus(String status);

    @Query("SELECT * FROM tasks WHERE name LIKE :name")
    List<Task> selectByName(String name);

    @Update
    int update(Task task);

    @Delete
    void delete(Task task);

    /**
     * wipes task table, use responsibly
     */
    @Query("DELETE FROM tasks")
    int delete();

    @Query("SELECT * FROM tasks WHERE objectId LIKE :taskID")
    Task selectByID(String taskID);

    @Query("Delete FROM tasks WHERE objectId LIKE :id")
    int deleteByID(String id);

    @RawQuery
    List<Task> searchTasksByQuery(SupportSQLiteQuery query);
}
