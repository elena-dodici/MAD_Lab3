package com.example.lab3.database

import android.content.Context
import androidx.room.*
import com.example.lab3.database.DAO.UserDao
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.Reservation
import com.example.lab3.database.entity.User
import com.example.lab3.database.DAO.CourtDao
import com.example.lab3.database.DAO.CourtReviewDAO
import com.example.lab3.database.DAO.CourtTimeDAO
import com.example.lab3.database.DAO.ReservationDAO
import com.example.lab3.database.DAO.SportDetailDAO
import com.example.lab3.database.entity.CourtReview
import com.example.lab3.database.entity.SportDetail

@Database(entities = [CourtTime::class, Court::class, Reservation::class, User::class, CourtReview::class, SportDetail::class], version = 1, exportSchema = false)
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