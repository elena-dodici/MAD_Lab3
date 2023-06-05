package com.example.lab3.database.entity

import androidx.room.*


data class CourtInfo(
//    var courtId:Int,
    var courtname:String ,
    var avg_rating:Float, //  values = [1/2/3/4/5]
    var address:String,
    var sport:String

)
