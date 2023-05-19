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

    //selected Reservation
    private var _selectedCourtRev = MutableLiveData<CourtReview>()
    val selectedCourtRev: LiveData<CourtReview> = _selectedCourtRev

    private var _showCourtInfo = MutableLiveData<CourtReview>()
    val showCourtInfo: LiveData<CourtReview> = _showCourtInfo

    private var _courtId = MutableLiveData<Int>()
    val courtId : LiveData<Int> = _courtId

    private var _courtRate = MutableLiveData<Float>()
    val courtRate : LiveData<Float> = _courtRate

    private var _courtName = MutableLiveData<String>()
    val courtName : LiveData<String> = _courtName

    private var _review = MutableLiveData<String>()
    val review : LiveData<String> = _review


    private var _userId = MutableLiveData<Int>()
    val userId : LiveData<Int> = _userId

    private var _hasRev = MutableLiveData<Boolean>()
    val hasRev : LiveData<Boolean> = _hasRev

    fun setSelectedCourtById(courtId:Int, avg_Rate: Float, application:Application){
        //courtInfo contains all courtid courtname and its avg_score
        for (r in _courtInfo.value!! ){
            if (r.courtId == courtId){
                _courtName.value = r.courtname
                _courtId.value = r.courtId
                _hasRev.value = false
                //0-xxx
                _courtRate.value = avg_Rate
                _review.value=""

                val reviewList = AppDatabase.getDatabase(application).courtReviewDao().getCourtReviewByCourtId(courtId)
                if (reviewList != null) {
                    for (rev in reviewList){
                        //_userId.value need to be updated once profile give userId
                        if (rev.userId == 1 ) {
                            _hasRev.value = true
                            //uid needed to be updated
                            _selectedCourtRev.value = AppDatabase.getDatabase(application).courtReviewDao().getCourtReviewByCourtIdUserId(courtId,1)
                            _review.value = _selectedCourtRev.value!!.review

                        }
                        else{
                            //this user never review this court
                            _selectedCourtRev.value!!.courtId = courtId
                            _selectedCourtRev.value!!.review = "Please leave your message"
                            _selectedCourtRev.value!!.userId = 1
                            _selectedCourtRev.value!!.rating = 0
                        }

                    }
                }


            }


        }
    }

    fun deleteCourtRev( application: Application){
         println(_selectedCourtRev.value)
        //AppDatabase.getDatabase(application).courtReviewDao().deleteCourtReview( _selectedCourtRev.value!!)
    }

    fun addOrUpdateCourtRev(newRate:Int, newReview:String, application: Application){

        var newCR = CourtReview(1,_courtId.value!!,newRate,newReview)
        println(newCR)
       // AppDatabase.getDatabase(application).courtReviewDao().addCourtReview(newCR)
    }

}