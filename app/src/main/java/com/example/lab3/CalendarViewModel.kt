package com.example.lab3

import android.app.Application
import android.widget.EditText
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.database.entity.MyReservation
import com.example.lab3.database.entity.Reservation
import org.jetbrains.annotations.NotNull
import java.sql.Date
import java.sql.Time
import java.time.LocalDate

class CalendarViewModel() : ViewModel( ) ,Observable{

    private var _reservations = MutableLiveData<List<MyReservation>>().also { it.value = listOf() }
    val reservations:LiveData<List<MyReservation>> = _reservations
    private var _courts = MutableLiveData<List<Court>>().also { it.value = listOf() }
    val courts:LiveData<List<Court>> = _courts
    lateinit var db: AppDatabase
    private var _resIdvm = MutableLiveData<Int>()
    val resIdvm : LiveData<Int> = _resIdvm
    private var _selCourtId = MutableLiveData<Int>()
    val selCourtId : LiveData<Int> = _selCourtId
    private var _selectedRes = MutableLiveData<MyReservation>()
    val selectedRes: LiveData<MyReservation> = _selectedRes
    private var _freeEndTimes = MutableLiveData<List<Time>>().also { it.value = listOf() }
    val freeEndTimes: LiveData<List<Time>> = _freeEndTimes
    private var _freeStartTimes = MutableLiveData<List<Time>>().also { it.value = listOf() }
    val freeStartTimes:LiveData<List<Time>> = _freeStartTimes
    private var _sportList = listOf("running", "basketball", "swimming","tennis")
    val sportList = _sportList
    fun getAllRes(application: Application){
        db = AppDatabase.getDatabase(application)
        _reservations.value = db.reservationDao().getReservationByUserId(1,1)
        getCourts(application)
    }
    fun getCourts(application: Application){
        db = AppDatabase.getDatabase(application)
        _courts.value = db.courtDao().getAllCourts()
    }
    fun getResBySport(application: Application,sport:String){
        db = AppDatabase.getDatabase(application)
        _reservations.value = db.reservationDao().getReservationBySport(sport)
    }
    fun setSelectedResByResId(resId:Int, application:Application){
        for (r in _reservations.value!! ){
            if (r.resId == resId) {
                _selectedRes.value = r

                _resIdvm.value = resId
                _selCourtId.value = r.courtId
                //getAllFreeSLotByCourtIdAndDate(r.courtId, r.date,0,application)
                val freeCourtTime = AppDatabase.getDatabase(application).courtDao().getAllFreeSlotByCourtIdAndDate(r.courtId, r.date,0)
                val freeEndTimeList = mutableListOf<Time>()
                val freStartTimeList = mutableListOf<Time>()
                for (i in freeCourtTime){
                    freeEndTimeList.add(i.endTime)
                    freStartTimeList.add(i.startTime)
                }

                freeEndTimeList.add(_selectedRes.value!!.endTime)
                freStartTimeList.add(_selectedRes.value!!.startTime)
                _freeEndTimes.value  = freeEndTimeList
                _freeStartTimes.value = freStartTimeList
            }
        }
    }

     fun getAllFreeSLotByCourtIdAndDate(courtId : Int, date:LocalDate, status:Int, application:Application){
       val freeCourtTime = AppDatabase.getDatabase(application).courtDao().getAllFreeSlotByCourtIdAndDate(courtId, date,status)
         val freeEndTimeList = mutableListOf<Time>()
         val freStartTimeList = mutableListOf<Time>()
         for (i in freeCourtTime){
             freeEndTimeList.add(i.endTime)
             freStartTimeList.add(i.startTime)
         }

         freeEndTimeList.add(_selectedRes.value!!.endTime)
         freStartTimeList.add(_selectedRes.value!!.startTime)
         _freeEndTimes.value  = freeEndTimeList
        _freeStartTimes.value = freStartTimeList
    }

    fun addOrUpdateRes(application: Application){
        //find courtTImdId
        val newCourtTime =  AppDatabase.getDatabase(application).courtTimeDao().getCourtTimeId(_selectedRes.value!!.startTime,_selectedRes.value!!.endTime)
        if (newCourtTime != null) {
            _selectedRes.value!!.courtTimeId = newCourtTime.id
        }

        val updateRes = Reservation(_selectedRes.value!!.courtTimeId, 1, 0,  _selectedRes.value!!.date,_selectedRes.value!!.description)
        updateRes.resId = _selectedRes.value!!.resId
        AppDatabase.getDatabase(application).reservationDao().addReservation(updateRes)

    }

    fun deleteRes(resId: Int, application: Application){
        AppDatabase.getDatabase(application).reservationDao().deleteReservation(resId)
    }

    fun clear(){
        _reservations.value = null
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }
}