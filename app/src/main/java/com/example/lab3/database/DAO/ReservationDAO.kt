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

//    // These functions are only for testing purposes ( comment or ignore if not needed)
//    @Query("SELECT * FROM reservation")
//    fun getAllReservationsTest(): List<Reservation>
//
//
//    @Query("SELECT * FROM reservation,(SELECT * FROM courtTime WHERE courtId=:id) AS cts " +
//            "WHERE reservation.courtTimeId = cts.id")
//    fun getReservationsByCourtId(id:Int): LiveData<List<Reservation>>


    @Query("SELECT * FROM reservation WHERE resId=:id")
    fun getReservationById(id:Int): Reservation?

    @Query("SELECT r.resId, name, address, sport, startTime, endTime, date, description,ct.id as courtTimeId,c.courtId\n" +
            "FROM reservation as r, court as c, courtTime as ct\n" +
            "WHERE r.courtTimeId=ct.id and r.status != :status and ct.courtId=c.courtId and userId=:id ")
    fun getReservationByUserId(status:Int, id:Int): List<MyReservation>?

    @Query("SELECT r.resId, name, address, sport, startTime, endTime, date, description,ct.id as courtTimeId,c.courtId\n" +
            "FROM reservation as r, court as c, courtTime as ct\n" +
            "WHERE r.courtTimeId=ct.id  and ct.courtId=c.courtId and sport=:sport ")
    fun getReservationBySport(sport:String): List<MyReservation>?
//    @Query("SELECT * FROM reservation WHERE userId=:id ")
//    fun getReservationByUserId(id:Int): List<Reservation>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addReservation(reservation: Reservation)

    //UPDATE Table_Name SET Column_Name = New_Value WHERE Condition;
    @Query("UPDATE reservation SET status = 1 WHERE resId=:id")
    fun deleteReservation(id:Int)


}