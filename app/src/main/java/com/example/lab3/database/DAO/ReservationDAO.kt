package com.example.lab3.database.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.CourtTime
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReservation(reservation: Reservation)

    @Delete
    fun deleteReservation(reservation: Reservation)

    @Update
    fun update(reservation: Reservation)
}