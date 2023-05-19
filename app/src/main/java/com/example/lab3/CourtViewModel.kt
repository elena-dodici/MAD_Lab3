package com.example.lab3

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase

class CourtViewModel : ViewModel( ) {
    lateinit var db: AppDatabase

    private var _courtInfo = MutableLiveData<List<CourtInfo>>().also { it.value = listOf() }
    val courtInfo:LiveData<List<CourtInfo>> = _courtInfo

    fun getCourtInfo(application: Application){
        db = AppDatabase.getDatabase(application)
//        _courtInfo.value = db.courtReviewDao().
//        拿到平均分和名字
    }
}