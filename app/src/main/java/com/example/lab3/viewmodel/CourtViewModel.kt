package com.example.lab3

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.CourtInfo
import com.example.lab3.database.entity.CourtReview
import com.example.lab3.database.entity.MyReservation
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Time
import java.time.Instant
import java.time.ZoneId

class CourtViewModel : ViewModel( ) {
    lateinit var db: AppDatabase
    val db1 = Firebase.firestore
    private val TAG = "CourtVM"

//    private var _courtreview = MutableLiveData<List<CourtReview>>().also { it.value = listOf() }
//    val courtreview:LiveData<List<CourtReview>> = _courtreview
    private var _courtInfo = MutableLiveData<List<CourtInfo>>().also { it.value = listOf() }
    val courtInfo:LiveData<List<CourtInfo>> = _courtInfo
    fun getCourtInfo(application: Application){
//        db = AppDatabase.getDatabase(application)
        val tempList:MutableList<CourtInfo> = mutableListOf()
//        tempList.addAll(db.courtReviewDao().getAllCourtReviews())
//        tempList.addAll(db.courtReviewDao().getCourtWithoutReview())
        db1.collection("court").get()
            .addOnSuccessListener { result ->
                val dataList = result.map { document ->
                    val courtName = mapOf(
                        "name" to document.id,
                    )
                    courtName + document.data
                }
                dataList.forEach{res-> // 遍历每一个reservation
//                    println(res)
                    tempList.add(CourtInfo(res["name"].toString(), res["avg_rating"].toString().toFloat()))
                }
//                _reservations.value = dataList
                _courtInfo.value = tempList
            }
            .addOnFailureListener { exception ->
                // 处理错误
                println("Error getting documents: ${exception.message}")
                _courtInfo.value = null
            }


      // println(_courtInfo.value)

    }


    //selected Reservation
    private var _selectedCourtInfo = MutableLiveData<CourtInfo>()
    val selectedCourtInfo: LiveData<CourtInfo> = _selectedCourtInfo

    private var _showCourtInfo = MutableLiveData<CourtReview>()
    val showCourtInfo: LiveData<CourtReview> = _showCourtInfo

//    private var _courtId = MutableLiveData<Int>()
//    val courtId : LiveData<Int> = _courtId

    private var _courtRate = MutableLiveData<Int>()
    val courtRate : LiveData<Int> = _courtRate

    private var _courtName = MutableLiveData<String>()
    val courtName : LiveData<String> = _courtName

    private var _review = MutableLiveData<String>()
    val review : LiveData<String> = _review


    private var _userId = MutableLiveData<Int>()
    val userId : LiveData<Int> = _userId

    private var _hasRev = MutableLiveData<Boolean>()
    val hasRev : LiveData<Boolean> = _hasRev

    fun setSelectedCourtById(courtName:String, avg_Rate: Float, user:Int){

        //courtInfo contains all courtid courtname and its avg_score
//        for (r in _courtInfo.value!! ){
//            if (r.courtId == courtId){
//                _courtName.value = r.courtname
//                _courtId.value = r.courtId
//                _hasRev.value = false
//                //0-xxx
//                val review = AppDatabase.getDatabase(application).courtReviewDao().getCourtReviewByCourtIdUserId(courtId,user)
//                if (review != null) {
//                    //_userId.value need to be updated once profile give userId
//                    //hasreview
//                    _hasRev.value = true
//                    //uid needed to be updated
//                    _selectedCourtRev.value = AppDatabase.getDatabase(application).courtReviewDao().getCourtReviewByCourtIdUserId(courtId,user)
//                    _courtRate.value = _selectedCourtRev.value!!.rating
////                    _review.value = _selectedCourtRev.value!!.review
//                }
//                else{
//                    //this user never review this court
//                    _selectedCourtRev.value =CourtReview(user,courtId,0,"")
//
//                }
//                }
//        }
    }

    fun setRate(newRate: Int){
        _courtRate.value = newRate
    }
    fun deleteCourtRev( ){
         println(selectedCourtInfo.value)
        db1.collection("court").document()
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        //AppDatabase.getDatabase(application).courtReviewDao().deleteCourtReview( _selectedCourtRev.value!!)
    }

//    fun addOrUpdateCourtRev(newRate:Int, newReview:String, application: Application,user: Int){
//
//        var newCR = CourtReview(user,_courtId.value!!,newRate,newReview)
//        println(newCR)
//        newCR.crId =  _selectedCourtRev.value!!.crId
//        println( newCR.crId)
//       AppDatabase.getDatabase(application).courtReviewDao().addCourtReview(newCR)
//    }

}