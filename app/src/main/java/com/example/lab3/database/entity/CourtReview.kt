package com.example.lab3.database.entity

import androidx.room.*

@Entity(tableName = "courtReview",
    indices = [Index(value = ["courtId"], unique = true), Index(value = ["userId"], unique = false)],
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
//    var courtname:String? =null,
//
//    @Ignore
//    var username:String? = null
){
    @PrimaryKey(autoGenerate = true)
    var crId:Int = 0
}
