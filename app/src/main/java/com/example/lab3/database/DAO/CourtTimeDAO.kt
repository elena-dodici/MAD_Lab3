package com.example.lab3.database.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.CourtTime

import java.sql.Time
import com.example.lab3.database.entity.FreeCourt


import java.time.LocalDate


@Dao
interface CourtTimeDAO {
    @Query("SELECT * FROM courtTime WHERE courtId=:id")
    fun getAllTCourtTimesByCourtId(id: Int): List<CourtTime>

    @Query("SELECT * FROM courtTime WHERE id=:id")
    fun getCourtTimeById(id:Int): CourtTime?

    @Query("SELECT id FROM courtTime WHERE courtId=:courtId AND startTime=:startTime")
    fun getCourtTimeIdByCourtIdAndStartTime(courtId: Int, startTime: Time) : Int

    // This function is only for testing purposes ( comment or ignore if not needed)

    @Query("SELECT * FROM courtTime WHERE startTime = :startTime AND endTime = :endTime")
    fun getCourtTimeId(startTime: Time, endTime:Time): CourtTime?


//    @Query("SELECT * FROM courtTime")
//    fun getAllTCourtTimes(): List<CourtTime>
    @Query("SELECT name,address,sport,startTime,endTime,courtTime.id as courtTimeId,court.courtId as courtId FROM courtTime,court WHERE courtTime.courtId=court.courtId AND sport=:sport")
    fun getAllTCourtTimes(sport:String): List<FreeCourt>
    // -------------------------------------------------------------------------- //


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCourtTime(courtTime: CourtTime)

    @Delete
    fun deleteCourtTime(courtTime: CourtTime)

    @Update
    fun update(courtTime: CourtTime)
}