package com.example.lab3.database.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lab3.database.entity.SportDetail

@Dao
interface SportDetailDAO {
    @Query("SELECT * FROM sportDetail")
    fun getAllSportDetails(): List<SportDetail>

    @Query("SELECT * FROM sportDetail WHERE sdId=:id")
    fun getSportDetailsById(id:Int): SportDetail?

    @Query("SELECT * FROM sportDetail WHERE userId=:id")
    fun getSportDetailsByUserId(id:Int): List<SportDetail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addSportDetails(sportDetail: SportDetail)

    @Delete
    fun deleteSportDetails(sportDetail : SportDetail)

    @Update
    fun updateSportDetails(sportDetail: SportDetail)
}
