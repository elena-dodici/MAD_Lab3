package com.example.lab3

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.Repository.AddReservationRepository
import com.example.lab3.database.entity.Reservation
import com.example.lab3.database.entity.ReservationFirebase
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.sql.Time
import java.time.LocalDate
import kotlin.coroutines.coroutineContext
import kotlin.properties.Delegates

class AddReservationViewModel : ViewModel() {
    lateinit var db: AppDatabase
    val db1 = Firebase.firestore
    val repo : AddReservationRepository = AddReservationRepository()
    private var _timeSlots = MutableLiveData<Map<Int,Boolean>>().also { it.value = mutableMapOf() }
    val timeSlots: LiveData<Map<Int,Boolean>> get() = _timeSlots
    private var slotsMap = mutableMapOf<Int, Boolean>()
    private lateinit var l : ListenerRegistration

    fun getTimeSlots(application: Application, sport : String, date : LocalDate){
        /*db = AppDatabase.getDatabase(application)
        val courtId = db.courtDao().getCourtIdBySport(sport)
        _timeSlots.value = db.courtDao().getFreeSlotsOfSpecificDateByCourtId(courtId,date)*/
        lateinit var courtName : String
        when(sport){
            "running" -> courtName = "court1"
            "basketball" -> courtName = "court2"
            "swimming" -> courtName = "court3"
            "pingpong" -> courtName = "court4"
            "tennis" -> courtName = "court5"
        }
        l = db1.collection("court").document(courtName).collection("courtTime").document(date.toString()).addSnapshotListener {
                value, error ->
            if(error != null) null
            else {
                value?.data?.forEach { d -> slotsMap.put(d.key.toInt(),d.value.toString().toBoolean()) }
                // SET THE VALUE TO UPDATE THE UI AND FILTER IT WITH SLOTS THAT ARE FREE
                _timeSlots.value = slotsMap.filter { x -> x.value == true }

            }
        }
    }


     fun addNewReservation(application: Application, reservation: ReservationFirebase, UID : String,date : String?,selectedSlot : String){
        /*try {
            db = AppDatabase.getDatabase(application)
            val newRes = db.reservationDao().addReservation(reservation)
        } catch (e : Exception){
            throw Exception(e.message)
        }*/

        // FIND NUMBER OF RESERVATIONS (NEEDED FOR THE NAME IN THE DB) AND START TRANSACTION TO CREATE
         // THE NEW RES AND UPDATE THE TIME SLOT STATUS TO "FALSE" ( BUSY )
         viewModelScope.launch {
             val numberOfReservations = repo.getNumberOfUserReservations(UID)
             println("NUMBER OF RESERVATIONS ${numberOfReservations} (USER ${UID})")
             repo.addResWithTransaction(reservation,UID,reservation.sport,date, selectedSlot,numberOfReservations)
         }
         // SAME OPERATIONS BUT WITHOUT TRANSACTION
        /* viewModelScope.launch {
             val numberOfReservations = repo.getNumberOfUserReservations(user)
             println("NUMBER OF RESERVATIONS ${numberOfReservations} (USER ${user})")
             val result = repo.addNewReservation(reservation,user,numberOfReservations)
             repo.updateTimeSlot(reservation.sport,date, selectedSlot)
         }*/

    }


    fun getCourtNameBySport(application: Application, sport: String) : String{
        /*val courtId = db.courtDao().getCourtIdBySport(sport)
        return courtId.toString()*/
        lateinit var courtName : String
        when(sport){
            "running" -> courtName = "court1"
            "basketball" -> courtName = "court2"
            "swimming" -> courtName = "court3"
            "pingpong" -> courtName = "court4"
            "tennis" -> courtName = "court5"
        }
       return courtName
    }

    fun getCourtTimeIdByCourtIdAndStartTime(application: Application,courtId : Int, startTime : Time) : Int{
        val courtTimeId = db.courtTimeDao().getCourtTimeIdByCourtIdAndStartTime(courtId,startTime)
        return courtTimeId
    }

    fun clear(){
        _timeSlots.value = null
    }
}