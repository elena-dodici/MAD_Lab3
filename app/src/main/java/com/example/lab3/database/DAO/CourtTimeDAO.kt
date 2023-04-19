package com.example.lab3.database.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.lab3.database.entity.CourtTime
@Dao
interface CourtTimeDAO {
    //write all query
    @Query("SELECT * FROM courtTime")
    fun getAll(): LiveData<List<CourtTime>>

    @Query("SELECT * FROM courtTime WHERE courtId=:id")
    fun getUserById(id:Int): CourtTime?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(CourtTime: CourtTime)

    @Delete
    fun delete(CourtTime: CourtTime)
}