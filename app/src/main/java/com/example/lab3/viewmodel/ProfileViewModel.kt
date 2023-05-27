package com.example.lab3

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.database.entity.MyReservation
import com.example.lab3.database.entity.SportDetail
import com.example.lab3.database.entity.User
import java.time.LocalDate

class ProfileViewModel : ViewModel() {

    lateinit var db: AppDatabase
    private var _User = MutableLiveData<User>()
    val User:LiveData<User> = _User
    private var _userSports = MutableLiveData<List<SportDetail>>().also { it.value = listOf() }
    val userSports:LiveData<List<SportDetail>> = _userSports

    fun getUserById(application: Application, id:Int){
        db = AppDatabase.getDatabase(application)
        _User.value = db.userDao().getUserById(id)
    }

    fun updateUser(application: Application, user: User,id: Int){
        db = AppDatabase.getDatabase(application)
        user.userId=id
        db.userDao().addUser(user)
    }

    fun getUserSportsById(application: Application, id:Int){
        db = AppDatabase.getDatabase(application)
        _userSports.value=db.sportDetailDao().getSportDetailsByUserId(id)
    }

    fun addUserSport(application: Application, sd:SportDetail){
        db = AppDatabase.getDatabase(application)
        db.sportDetailDao().addSportDetails(sd)
    }

    fun updateUserSport(application: Application, sd: SportDetail,id:Int){
        db = AppDatabase.getDatabase(application)
        sd.userId=id
        db.sportDetailDao().updateSportDetails(sd)
    }

    fun deleteUserSport(application: Application, sd: SportDetail){
        db = AppDatabase.getDatabase(application)
        db.sportDetailDao().deleteSportDetails(sd)
    }
}