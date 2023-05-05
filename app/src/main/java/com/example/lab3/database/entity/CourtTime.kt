package com.example.lab3.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Time

@Entity(tableName = "courtTime")
data class CourtTime(


    @ColumnInfo(name = "courtId")
    val courtId:Int = 0,

    @ColumnInfo(name = "startTime") // all available time slot: 9:00 -> 19:00
    var startTime:Time,

    @ColumnInfo(name = "endTime")
    var endTime:Time
){
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}
