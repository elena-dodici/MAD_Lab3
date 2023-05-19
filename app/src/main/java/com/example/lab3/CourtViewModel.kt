package com.example.lab3

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.CourtInfo
import com.example.lab3.database.entity.CourtReview

class CourtViewModel : ViewModel( ) {
    lateinit var db: AppDatabase

//    private var _courtreview = MutableLiveData<List<CourtReview>>().also { it.value = listOf() }
//    val courtreview:LiveData<List<CourtReview>> = _courtreview
    private var _courtInfo = MutableLiveData<List<CourtInfo>>().also { it.value = listOf() }
    val courtInfo:LiveData<List<CourtInfo>> = _courtInfo
    fun getCourtInfo(application: Application){
        db = AppDatabase.getDatabase(application)
        _courtInfo.value = db.courtReviewDao().getAllCourtReviews()

    }
}