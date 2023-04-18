package com.example.lab3.database.entity

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "reservation" )
data class Reservation(
    @PrimaryKey(autoGenerate = true)
    var res_id : Int,

    @ColumnInfo(name = "court_id")
    var court_id:Int, // id of pg subtable

    @ColumnInfo(name = "user_id")
    var user_id:Int,

    @ColumnInfo(name = "status")
    var status:Int, // 0: normal reservation.  1: canceled reservation

    @ColumnInfo(name = "date")
    var date: Long
)
