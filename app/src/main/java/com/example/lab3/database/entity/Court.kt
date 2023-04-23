package com.example.lab3.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "court")
data class Court(
    @PrimaryKey(autoGenerate = true)
    var courtId:Int,

    @ColumnInfo(name = "name")
    var name:String,

    @ColumnInfo(name = "address")
    var address:String,

    @ColumnInfo(name = "sport")
    var sport:String
)

