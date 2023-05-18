package com.example.lab3.database.DAO

import androidx.room.*
import com.example.lab3.database.entity.SportDetail

interface SportDetailDAO {
    @Query("SELECT * FROM sportDetail")
    fun getAllSportDetails(): List<SportDetail>

    @Query("SELECT * FROM sportDetail WHERE sdId=:id")
    fun getSportDetailsById(id:Int): SportDetail?

    @Query("SELECT * FROM sportDetail WHERE userId=:id")
    fun getSportDetailsByUserId(id:Int): SportDetail?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSportDetails(sportDetail: SportDetail)

    @Delete
    fun deleteSportDetails(sportDetail : SportDetail)

    @Update
    fun updateSportDetails(sportDetail: SportDetail)
}
