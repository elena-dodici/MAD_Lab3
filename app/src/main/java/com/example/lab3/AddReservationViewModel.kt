package com.example.lab3

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.database.entity.MyReservation

class AddReservationViewModel : ViewModel() {
    lateinit var db: AppDatabase
    private var _timeSlots = MutableLiveData<List<CourtTime>>().also { it.value = listOf() }
    val timeSlots: LiveData<List<CourtTime>> = _timeSlots

    fun getTimeSlots(application: Application, courtId : Int){
        db = AppDatabase.getDatabase(application)
        _timeSlots.value = db.courtDao().getAllCourtFreeSlotsByCourtIdTest(courtId)
    }


    fun clear(){
        _timeSlots.value = null
    }
}