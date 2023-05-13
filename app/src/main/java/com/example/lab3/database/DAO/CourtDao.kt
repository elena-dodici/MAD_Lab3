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
import com.example.lab3.database.entity.FreeCourt
import com.example.lab3.database.entity.Reservation
import java.time.LocalDate


@Dao
interface CourtDao {
    @Query("SELECT * FROM court")
    fun getAllCourts():List<Court>

    // These functions are only for testing purposes ( comment or ignore if not needed)
    @Query("SELECT * FROM court")
    fun getAllCourtsTest(): List<Court>


//    @Query("SELECT * FROM courtTime where id  NOT IN (SELECT courtTimeId from reservation ) AND courtId  = :id ")
//    fun getAllCourtFreeSlotsByCourtId(id:Int) : List<CourtTime>

    @Query("SELECT * FROM courtTime where id NOT IN  (\n" +
            "SELECT id\n" +
            "FROM courtTime \n" +
            "LEFT JOIN reservation ON courtTime.id = reservation.courtTimeId WHERE courtId=:id AND reservation.date = :date\n" +
            " AND reservation.status = :status \n" +
            ") AND  courtId= :id")
    fun getAllFreeSlotByCourtIdAndDate(id: Int, date:LocalDate, status: Int): List<CourtTime>

    // 关于某个运动，在这一天没有预约的时间段
    @Query("SELECT name,address,sport,startTime,endTime,courtTime.id as courtTimeId,court.courtId as courtId \n" +
            "FROM court , courtTime\n" +
            "WHERE sport =:sport AND court.courtId = courtTime.courtId AND courtTime.id NOT IN(\n" +
            "        SELECT courtTimeId\n" +
            "        FROM reservation \n" +
            "        WHERE userId=1 AND status=0 AND date =:date)")
    fun getFreeSlotBySportAndDate(sport: String, date:LocalDate): List<FreeCourt>

    @Query("SELECT courtId FROM court WHERE sport=:sport")
    fun getCourtIdBySport(sport: String) : Int

    @Query("SELECT * FROM courtTime WHERE id NOT IN (SELECT id FROM courtTime JOIN reservation ON courtTime.id = reservation.courtTimeId " +
            "WHERE courtId=:courtId AND date=:date) AND courtId=:courtId")
    fun getFreeSlotsOfSpecificDateByCourtId(courtId : Int, date : LocalDate) : List<CourtTime>

//    @Query("SELECT * FROM courtTime WHERE id NOT IN (SELECT id FROM courtTime JOIN reservation ON courtTime.id = reservation.courtTimeId " +
//            "WHERE courtId=:id)")
//    fun getAllCourtFreeSlotsByCourtIdTest(id:Int) : List<CourtTime>
//    // -------------------------------------------------------------------------- //
//    @Query("SELECT * FROM courtTime WHERE id NOT IN (SELECT id FROM courtTime JOIN reservation ON courtTime.id = reservation.courtTimeId " +
//            "WHERE courtId=:id)")
//    fun getAllCourtFreeSlotsByCourtId(id:Int) : LiveData<List<CourtTime>>



    @Query("SELECT * FROM court WHERE courtId=:id")
    fun getCourtById(id:Int): Court?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCourt(court: Court)
    @Query("SELECT * FROM court WHERE sport=:sport")
    fun getCourtBySport(sport:String): List<Court>
    @Delete
    fun deleteCourt(court:Court)

    @Update
    fun update(court:Court)
}