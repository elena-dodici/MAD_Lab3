package com.example.lab3

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab3.database.Repository.AddReservationRepository
import com.example.lab3.database.Repository.MainRepository
import com.example.lab3.database.entity.ReservationFirebase
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainViewModel: ViewModel() {

    private var _showNav = MutableLiveData<Boolean>()
    val showNav : LiveData<Boolean> = _showNav
    val repo : MainRepository = MainRepository()

    var user:Int=1
    var UID = "PUAcFMgi07ZZCc0fZml4xF5Zed93"

    fun updateCourtTimesDates(){
        viewModelScope.launch {
            val lastDay = repo.getLastDayInCourtTimes() // FORMAT yyyy-MM-dd

            val currentDate : LocalDate = LocalDate.now()
            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val currentDayPlus30 = currentDate.plusDays(30)
            val formattedCurrentDatePlus30: String = currentDayPlus30.format(formatter)
            if(formattedCurrentDatePlus30 == lastDay){
                // DO NOTHING
                println("The last day is equal to the actual day + 30 - no updated necessary")
            } else {
                // ADD DAYS
                val lDofLastDay: LocalDate = LocalDate.parse(lastDay, formatter)
                var updatedLastDayLD : LocalDate = lDofLastDay
                var updatedLastDayString: String = lDofLastDay.toString()

                while(updatedLastDayString != formattedCurrentDatePlus30){
                println("adding days....")
                    updatedLastDayLD = updatedLastDayLD.plusDays(1)
                    updatedLastDayString = updatedLastDayLD.toString()
                    for(i in listOf<Int>(1,2,3,4,5)){
                        println(i)
                        repo.addDayInDatabase(updatedLastDayString,"court${i}")
                    }
            }
                println("ldOfLastDay : ${lDofLastDay}")
                println("updatedLast day : ${updatedLastDayString}")
                println("Last Available day is : ${lastDay}")
                println("LAST DAY + 30 : ${formattedCurrentDatePlus30}")
            }

        }


    }
    fun setShowNav(vis:Boolean){
        _showNav.value = vis
    }
}