package com.example.lab3.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courtTime")
data class CourtTime(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,

    @ColumnInfo(name = "courtId")
    val courtId:Int = 0,

    @ColumnInfo(name = "startTime")
    var startTime:String,

    @ColumnInfo(name = "endTime")
    var endTime:String
)
