package com.example.lab3.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(


    @ColumnInfo(name = "name")
    var name:String,

    @ColumnInfo(name = "surname")
    var surname:String,

    @ColumnInfo(name = "tel")
    var tel:String?

){
    @PrimaryKey(autoGenerate = true)
    var userId:Int = 0;
}