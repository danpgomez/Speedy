package com.e.speedy.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_run_quality_table")
data class Run(
    @PrimaryKey(autoGenerate = true)
    var runId: Long = 0L,
    @ColumnInfo(name = "start_time_milli")
    val startTimeMillis: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "end_time_milli")
    var endTimeMillis: Long = startTimeMillis,

    @ColumnInfo(name = "quality_rating")
    var runQuality: Int = -1
) {
}