package com.example.lab3.database.entity

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import org.jetbrains.annotations.NotNull
import java.sql.Date
import java.time.LocalDate

@Entity(tableName = "reservation",
    indices = [Index(value = ["courtTimeId"], unique = true),Index(value = ["userId"], unique = false)],
foreignKeys = [
    ForeignKey(
        entity = CourtTime::class,
        childColumns = ["courtTimeId"],
        parentColumns = ["id"]),
    ForeignKey(
        entity = User::class,
        childColumns = ["userId"],
        parentColumns = ["userId"]
    )
])
data class Reservation(

    @ColumnInfo(name = "courtTimeId")
    var courtTimeId:Int, // id of table court_time 对应了某个球场的一个时间段

    @ColumnInfo(name = "userId")
    var userId:Int,

    @ColumnInfo(name = "status")
    var status:Int, // 0: normal reservation.  1: canceled reservation

    @ColumnInfo(name = "date")
    var date: LocalDate,//dd/mm/yyyy

    @ColumnInfo(name = "description")
    var description: String
){
    @PrimaryKey(autoGenerate = true)
    var resId : Int = 0
}
