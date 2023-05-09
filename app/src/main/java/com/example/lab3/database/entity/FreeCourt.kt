package com.example.lab3.database.entity

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import java.sql.Date
import java.sql.Time
import java.time.LocalDate


data class FreeCourt(
    var name:String,
    var address:String,
    var sport:String,
    var startTime: Time,
    var endTime: Time,
    var courtTimeId: Int,
    var courtId: Int

    )