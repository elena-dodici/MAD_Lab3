package com.example.lab3.database.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.database.entity.Reservation


@Dao
interface CourtDao {
    @Query("SELECT * FROM court")
    fun getAllCourts(): LiveData<List<Court>>

    // These functions are only for testing purposes ( comment or ignore if not needed)
    @Query("SELECT * FROM court")
    fun getAllCourtsTest(): List<Court>

    @Query("SELECT * FROM courtTime WHERE id NOT IN (SELECT id FROM courtTime JOIN reservation ON courtTime.id = reservation.courtTimeId " +
            "WHERE courtId=:id)")
    fun getAllCourtFreeSlotsByCourtIdTest(id:Int) : List<CourtTime>
    // -------------------------------------------------------------------------- //
    @Query("SELECT * FROM courtTime WHERE id NOT IN (SELECT id FROM courtTime JOIN reservation ON courtTime.id = reservation.courtTimeId " +
            "WHERE courtId=:id)")
    fun getAllCourtFreeSlotsByCourtId(id:Int) : LiveData<List<CourtTime>>

    @Query("SELECT * FROM court WHERE courtId=:id")
    fun getCourtById(id:Int): Court?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCourt(court: Court)

    @Delete
    fun deleteCourt(court:Court)

    @Update
    fun update(court:Court)
}