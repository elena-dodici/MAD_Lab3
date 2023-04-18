package com.example.lab3.database

import android.content.Context
import androidx.room.*
import com.example.lab3.database.DAO.UserDao
import com.example.lab3.database.entity.Court_time
import com.example.lab3.database.entity.Playground
import com.example.lab3.database.entity.Reservation
import com.example.lab3.database.entity.User
import com.example.lab3.database.Converter

@Database(entities = [Court_time::class, Playground::class, Reservation::class, User::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase:RoomDatabase() {
//    abstract fun CTDao():CTDao
//    abstract fun PGDao():PGDao
//    abstract fun ResDao():ResDao
    abstract fun userDao():UserDao
    companion object{
        @Volatile
        private var INSTANCE:AppDatabase? = null
        fun getDatabase(context:Context):AppDatabase=
            (INSTANCE?:
            synchronized(this){
                val i = INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                                                        AppDatabase::class.java,
                                                        "AppDatabase")
                    .allowMainThreadQueries()
//                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = i
                INSTANCE
            })!!
    }
}
//val db = AppDatabase.getDatabase(application)