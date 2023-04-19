package com.example.lab3.database.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab3.database.entity.Court


@Dao
interface CourtDao {
    //write all query
    @Query("SELECT * FROM court")
    fun getAll(): LiveData<List<Court>>

    @Query("SELECT * FROM court WHERE courId=:id")
    fun getUserById(id:Int): Court?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(Court: Court)

    @Delete
    fun delete(Court:Court)


}