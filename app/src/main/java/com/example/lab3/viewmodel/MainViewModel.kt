package com.example.lab3

import android.app.Application
import android.widget.Toast
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
import com.google.firebase.auth.FirebaseAuth

class MainViewModel: ViewModel() {

    private var _showNav = MutableLiveData<Boolean>()
    val showNav : LiveData<Boolean> = _showNav
    val repo : MainRepository = MainRepository()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var currentToast : Toast
    var user:Int=1
    var UID = ""

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
    interface LoginCallback {
        fun onLoginSuccess(uid: String)
        fun onLoginFailure()
    }
    fun login(application: Application, email:String,password:String,callback: LoginCallback){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    if(auth.currentUser!!.isEmailVerified){
                        UID = auth.currentUser!!.uid
                        println("UID --"+ UID)
                        callback.onLoginSuccess(UID)
                    }else{
                        callback.onLoginFailure()
                        Toast.makeText(application, "Your email has not been verified.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    callback.onLoginFailure()
                    // If sign in fails, display a message to the user.
                    Toast.makeText(application, "Login failed. Check you password and email and make sure your email is verified.", Toast.LENGTH_SHORT).show()
                }
            }

    }
    fun logOut (application: Application){
        auth.signOut()
        UID=""
    }

    fun setShowNav(vis:Boolean){
        _showNav.value = vis
    }
}