package com.example.lab3.database.entity

import androidx.room.*

@Entity(tableName = "courtReview",
    indices = [Index(value = ["courtId"], unique = true), Index(value = ["userId"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Court::class,
            childColumns = ["courtId"],
            parentColumns = ["courtId"]),
        ForeignKey(
            entity = User::class,
            childColumns = ["userId"],
            parentColumns = ["userId"]
        )
    ])
data class CourtReview(

    @ColumnInfo(name = "userId")
    var userId:Int,

    @ColumnInfo(name = "courtId")
    var courtId:Int,

    @ColumnInfo(name = "rating")
    var rating:Int, //  values = [1/2/3/4/5]

    @ColumnInfo(name = "review")
    var review:String,

//    @Ignore
//    var courtName:String =""
){
    @PrimaryKey(autoGenerate = true)
    var crId:Int = 0
}
