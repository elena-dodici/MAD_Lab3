package com.example.lab3.database.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lab3.database.entity.Reservation
@Dao
interface ReservationDAO {
    //write all query
    @Query("SELECT * FROM reservation")
    fun getAll(): LiveData<List<Reservation>>

    @Query("SELECT * FROM reservation WHERE resId=:id")
    fun getUserById(id:Int): Reservation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(Reservation: Reservation)

    @Delete
    fun delete(Reservation: Reservation)
}