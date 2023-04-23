package com.example.lab3.database.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

import androidx.room.Query
import com.example.lab3.database.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE userId=:id")
    fun getUserById(id:Int):User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user:User)

}