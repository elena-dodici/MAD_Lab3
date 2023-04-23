package com.example.lab3.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courtTime")
data class CourtTime(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,

    @ColumnInfo(name = "court_id")
    val courtId:Int = 0,

    @ColumnInfo(name = "start_time")
    var startTime:String,

    @ColumnInfo(name = "end_time")
    var endTime:String
)
