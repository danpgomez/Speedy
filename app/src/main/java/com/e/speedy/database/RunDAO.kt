package com.e.speedy.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RunDAO {
    @Insert
    fun insert(run: Run)

    @Update
    fun update(run: Run)

    @Query("SELECT * FROM daily_run_quality_table WHERE runId = :key")
    fun get(key: Long): Run?

    @Query("DELETE FROM daily_run_quality_table")
    fun clear()

    @Query("SELECT * FROM daily_run_quality_table ORDER BY runId DESC")
    fun getAllRuns(): LiveData<List<Run>>

    @Query("SELECT * FROM daily_run_quality_table ORDER BY runId DESC LIMIT 1")
    fun getCurrentRun(): Run?
}