package com.example.lab3.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playground")
data class Playground(
    @PrimaryKey(autoGenerate = true)
    var pg_id:Int,

    @ColumnInfo(name = "name")
    var name:String,

    @ColumnInfo(name = "address")
    var address:String,

    @ColumnInfo(name = "sport")
    var sport:String
)
