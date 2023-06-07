package com.example.lab3.database.entity

import java.sql.Timestamp

data class ReservationFirebase(
    val startTime : Timestamp,
    val endTime : Timestamp,
    val description : String,
    val court : String,
    val sport : String,
    val status : Int ,// 0 normal , 1 canceled
    val review : String,
    val rating : Int,
    val invitedUsers : List<String>
)