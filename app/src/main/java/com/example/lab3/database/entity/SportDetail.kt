package com.example.lab3.database.entity

import androidx.room.*

@Entity(tableName = "sportDetail",
    indices = [Index(value = ["userId"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            childColumns = ["userId"],
            parentColumns = ["userId"])
    ])
data class SportDetail(

    @ColumnInfo(name = "userId")
    var userId:String,

    @ColumnInfo(name = "masteryLevel")
    var masteryLevel:Int, // values = [0/1/2] -> beginner / intermediate / expert

    @ColumnInfo(name = "achievement")
    var achievement:String
){
    @PrimaryKey(autoGenerate = true)
    var sdId:Int = 0
}
