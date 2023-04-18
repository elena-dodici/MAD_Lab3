package com.example.lab3.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "court_time")
data class Court_time(
    @PrimaryKey(autoGenerate = true)
    val court_id:Int = 0,

    @ColumnInfo(name = "start_time")
    var start_time:Long,

    @ColumnInfo(name = "end_time")
    var end_time:Long
)
