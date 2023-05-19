package com.example.lab3.database

import android.content.Context
import androidx.room.*
import com.example.lab3.database.DAO.*
import com.example.lab3.database.entity.*

@Database(entities = [CourtTime::class, Court::class, Reservation::class, User::class, CourtReview::class,SportDetail::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase:RoomDatabase() {
    abstract fun userDao():UserDao
    abstract fun courtDao():CourtDao
    abstract fun reservationDao():ReservationDAO
    abstract fun courtTimeDao():CourtTimeDAO
    abstract fun courtReviewDao():CourtReviewDAO
    abstract fun sportDetailDao():SportDetailDAO

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
//                   .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = i
                INSTANCE
            })!!
    }
}
//val db = AppDatabase.getDatabase(application)