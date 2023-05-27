package com.example.lab3

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.database.entity.MyReservation
import com.example.lab3.database.entity.Reservation
import java.sql.Time
import java.time.LocalDate

class AddReservationViewModel : ViewModel() {
    lateinit var db: AppDatabase
    private var _timeSlots = MutableLiveData<List<CourtTime>>().also { it.value = listOf() }
    val timeSlots: LiveData<List<CourtTime>> get() = _timeSlots


    fun getTimeSlots(application: Application, sport : String, date : LocalDate){
        db = AppDatabase.getDatabase(application)
        val courtId = db.courtDao().getCourtIdBySport(sport)
        _timeSlots.value = db.courtDao().getFreeSlotsOfSpecificDateByCourtId(courtId,date)
    }


    fun addNewReservation(application: Application, reservation: Reservation){
        try {
            db = AppDatabase.getDatabase(application)
            val newRes = db.reservationDao().addReservation(reservation)
        } catch (e : Exception){
            throw Exception(e.message)
        }
    }

    fun getCourtIdBySport(application: Application, sport: String) : Int{
        val courtId = db.courtDao().getCourtIdBySport(sport)
        return courtId
    }

    fun getCourtTimeIdByCourtIdAndStartTime(application: Application,courtId : Int, startTime : Time) : Int{
        val courtTimeId = db.courtTimeDao().getCourtTimeIdByCourtIdAndStartTime(courtId,startTime)
        return courtTimeId
    }

    fun clear(){
        _timeSlots.value = null
    }
}