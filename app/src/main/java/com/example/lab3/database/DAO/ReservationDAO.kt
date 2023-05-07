package com.example.lab3.database.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.database.entity.MyReservation
import com.example.lab3.database.entity.Reservation
@Dao
interface ReservationDAO {
    @Query("SELECT * FROM reservation")
    fun getAllReservations(): LiveData<List<Reservation>>

    // This function is only for testing purposes ( comment or ignore if not needed)
    @Query("SELECT * FROM reservation")
    fun getAllReservationsTest(): List<Reservation>
    // -------------------------------------------------------------------------- //

    @Query("SELECT * FROM reservation WHERE resId=:id")
    fun getReservationById(id:Int): Reservation?

    @Query("SELECT name, address, sport, startTime, endTime, date, description\n" +
            "FROM reservation as r, court as c, courtTime as ct\n" +
            "WHERE r.courtTimeId=ct.id and ct.courtId=c.courtId and userId=:id ")
    fun getReservationByUserId(id:Int): List<MyReservation>?
//    @Query("SELECT * FROM reservation WHERE userId=:id ")
//    fun getReservationByUserId(id:Int): List<Reservation>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReservation(reservation: Reservation)

    @Delete
    fun deleteReservation(reservation: Reservation)

    @Update
    fun update(reservation: Reservation)
}