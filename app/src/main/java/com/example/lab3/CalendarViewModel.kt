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
import com.example.lab3.database.entity.MyReservation
import com.example.lab3.database.entity.Reservation
import org.jetbrains.annotations.NotNull
import java.sql.Date
import java.time.LocalDate

class CalendarViewModel() : ViewModel( ) ,Observable{

    private var _reservations = MutableLiveData<List<MyReservation>>().also { it.value = listOf() }
    val reservations:LiveData<List<MyReservation>> = _reservations
    lateinit var db: AppDatabase

    fun getAllRes(application: Application){
        db = AppDatabase.getDatabase(application)
        _reservations.value = db.reservationDao().getReservationByUserId(1,1)

    }

    private var _resIdvm = MutableLiveData<Int>()
    val resIdvm : LiveData<Int> = _resIdvm


    private val _selectedRes = MutableLiveData<MyReservation>()
    val selectedRes: LiveData<MyReservation> = _selectedRes
    fun setSelectedResByResId(resId:Int){
        for (r in _reservations.value!! ){
            if (r.resId == resId)  _selectedRes.value = r
            _resIdvm.value = resId
        }

    }


    fun addOrUpdateRes(application: Application){
        println("let me check whether save success ------")
        println(_selectedRes.value!!.date)
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