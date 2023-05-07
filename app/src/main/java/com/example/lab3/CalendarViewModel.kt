package com.example.lab3

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.MyReservation
import com.example.lab3.database.entity.Reservation
import org.jetbrains.annotations.NotNull

class CalendarViewModel() : ViewModel( ) {

    private var _reservations = MutableLiveData<List<MyReservation>>().also { it.value = listOf() }
    val reservations:LiveData<List<MyReservation>> = _reservations
    lateinit var db: AppDatabase

    fun getAllRes(application: Application){
        db = AppDatabase.getDatabase(application)
        _reservations.value = db.reservationDao().getReservationByUserId(1)

    }
    fun clear(){
        _reservations.value = null
    }
}