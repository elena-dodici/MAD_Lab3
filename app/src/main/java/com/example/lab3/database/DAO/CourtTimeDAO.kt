package com.example.lab3.database.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.CourtTime
import java.sql.Time
import java.time.LocalDate

@Dao
interface CourtTimeDAO {
    @Query("SELECT * FROM courtTime WHERE courtId=:id")
    fun getAllTCourtTimesByCourtId(id: Int): LiveData<List<CourtTime>>

    @Query("SELECT * FROM courtTime WHERE id=:id")
    fun getCourtTimeById(id:Int): CourtTime?

    @Query("SELECT id FROM courtTime WHERE courtId=:courtId AND startTime=:startTime")
    fun getCourtTimeIdByCourtIdAndStartTime(courtId: Int, startTime: Time) : Int

    // This function is only for testing purposes ( comment or ignore if not needed)
    @Query("SELECT * FROM courtTime WHERE courtId=:id")
    fun getAllTCourtTimesByCourtIdTest(id: Int): List<CourtTime>

    // -------------------------------------------------------------------------- //

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCourtTime(courtTime: CourtTime)

    @Delete
    fun deleteCourtTime(courtTime: CourtTime)

    @Update
    fun update(courtTime: CourtTime)
}