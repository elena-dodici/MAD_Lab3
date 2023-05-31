package com.example.lab3

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.FreeCourt
import com.example.lab3.database.entity.MyReservation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Time
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

class  CalendarViewModel() : ViewModel( ) {
    private val TAG = "CalendarVM"

    //reservationList of this user
    private var _reservations = MutableLiveData<List<MyReservation>>().also { it.value = listOf() }
    val reservations: LiveData<List<MyReservation>> = _reservations

    //allCourt
    private var _courts = MutableLiveData<List<Court>>().also { it.value = listOf() }
    val courts: LiveData<List<Court>> = _courts
    lateinit var db: AppDatabase

    //reservationId
    private var _resIdvm = MutableLiveData<Int>()
    val resIdvm: LiveData<Int> = _resIdvm

    //selected Reservation
    private var _selectedRes = MutableLiveData<MyReservation>()
    val selectedRes: LiveData<MyReservation> = _selectedRes


    //selected Date
    private var _selDate = MutableLiveData<LocalDate>()
    val selDate: LiveData<LocalDate> = _selDate

    private var _selStartTime = MutableLiveData<Time>()
    val selStartTime: LiveData<Time> = _selStartTime
//    private var _selEndTime = MutableLiveData<Time>()
//    val selEndTime: LiveData<Time> = _selEndTime

    private var _selDes = MutableLiveData<String>()
    val selDes: LiveData<String> = _selDes

//    private var _freeEndTimes = MutableLiveData<List<Time>>().also { it.value = listOf() }
//    val freeEndTimes: LiveData<List<Time>> = _freeEndTimes
    private var _freeStartTimes = MutableLiveData<List<Time>>().also { it.value = listOf() }
    val freeStartTimes: LiveData<List<Time>> = _freeStartTimes

    private var _sportList = listOf("running", "basketball", "swimming", "tennis", "pingpong")
    val sportList = _sportList

    private var _freeSlots = MutableLiveData<List<FreeCourt>>().also { it.value = listOf() }
    val freeSlots = _freeSlots

    private var _F = MutableLiveData<List<FreeCourt>>().also { it.value = listOf() }
    val F: LiveData<List<FreeCourt>> = _F

    private var _courtNames = MutableLiveData<List<String>>().also { it.value = listOf() }
    val courtNames: LiveData<List<String>> = _courtNames

    val db1 = Firebase.firestore


    private var _selCourtName = MutableLiveData<String>()
    val selCourtName: LiveData<String> = _selCourtName

    private var _selSport = MutableLiveData<String>()
    val selSport: LiveData<String> = _selSport


    fun getAllRes(application: Application, userid: Int) {
//        db = AppDatabase.getDatabase(application)

        db1.collection("users").document("u${userid}").collection("reservation").get()
            .addOnSuccessListener { result ->

                val dataList = result.map { document ->
                    val mapId = mapOf(
                        "resId" to document.id,
                    )
                    mapId + document.data
                }
//                println("result ${dataList}")
                val myres = mutableListOf<MyReservation>()
                dataList.forEach { res -> // 遍历每一个reservation
//                    println(res["name"].toString())

                    val ct = res["ct"] as Map<*, *>
                    val starttimeStr = ct["startTime"].toString()
                    val endtimeStr = ct["endTime"].toString()
                    // 使用正则表达式提取秒数值
                    val regex = "seconds=(\\d+)".toRegex()
                    var matchResult = regex.find(starttimeStr)
                    val secondsStart = matchResult?.groupValues?.get(1)?.toLongOrNull()

                    // 将秒数值转换为 Timestamp 对象
//                    val starttimestamp = seconds?.let { Timestamp(it, 0) }

                    val zone = ZoneId.of("UTC+2")
//                    val zone = ZoneId.systemDefault()
                    val startTime = Instant.ofEpochSecond(secondsStart!!).atZone(zone)

                    matchResult = regex.find(endtimeStr)
                    val secondsEnd = matchResult?.groupValues?.get(1)?.toLongOrNull()
//                    val endtimestamp = seconds?.let { Timestamp(it,0) }
                    val endTime = Instant.ofEpochSecond(secondsEnd!!).atZone(zone)

//                    println(res["description"].toString())
                    if (res["status"].toString() == "0") {
                        myres.add(
                            MyReservation(
                                res["resId"].toString(),
                                res["name"].toString(),
                                res["sport"].toString(),
                                Time(startTime.hour, startTime.minute, startTime.second),
                                Time(endTime.hour, endTime.minute, endTime.second),
                                startTime.toLocalDate(),
                                res["description"].toString(),
                                res["review"].toString(),
                                res["rating"].toString().toInt()
                            )

                        )
                    }

                }
                _reservations.value = myres
//                _reservations.value = dataList
            }
            .addOnFailureListener { exception ->
                // 处理错误
                println("Error getting documents: ${exception.message}")
                _reservations.value = null
            }
//        _reservations.value = db.reservationDao().getReservationByUserId(userid)
//        getCourts(application)
    }

    fun getCourts(application: Application) {
        db = AppDatabase.getDatabase(application)
        _courts.value = db.courtDao().getAllCourts()
    }

    fun getResBySport(application: Application, sport: String) {
        db = AppDatabase.getDatabase(application)
//        _reservations.value = db.reservationDao().getReservationBySport(sport)
    }


    fun getAllCourtTime(application: Application, sport: String) {
        db = AppDatabase.getDatabase(application)
        _F.value = db.courtTimeDao().getAllTCourtTimes(sport)
    }
    var freeStartTimeList = mutableListOf<Time>()
    val freeEndTimeList = mutableListOf<Time>()

    fun setSelectedResByCourtName(resId: String) {


        for (r in _reservations.value!!) {
            if (r.resId == resId) {

                _selectedRes.value = r
                _selDate.value = r.date
//                _resIdvm.value = resId
//                _selCourtId.value = r.courtId
                _selStartTime.value = r.startTime

                _selCourtName.value = r.name
                _selDes.value = r.description
                _selSport.value = r.sport


                db1.collection("court").document(r.name).collection("courtTime")
                    .document("${r.date}").get()
                    .addOnSuccessListener { result ->
                        if (result != null) {
                            result.data?.forEach { ct ->
                                if (ct.value == true) {

                                    freeStartTimeList.add(Time(ct.key.toInt(), 0,0))
                                    freeEndTimeList.add(Time(ct.key.toInt() + 1, 0,0))
                                }

                            }

                            freeStartTimeList.add(_selectedRes.value!!.startTime)
                            //println(" freeStartTimeList is : ${freeStartTimeList}")
                            _freeStartTimes.value = freeStartTimeList
                            println("bug ${selectedRes.value!!.rating}")
                        } else {
                            Log.d(TAG, "No such document")
                        }
                    }  .addOnFailureListener { exception ->
                                // 处理错误
                                Log.d(TAG,"Error getting documents: ${exception.message}")

                            }
//                //getAllFreeSLotByCourtIdAndDate(r.courtId, r.date,0,application)
//                val freeCourtTime = AppDatabase.getDatabase(application).courtDao().getAllFreeSlotByCourtIdAndDate(r.courtId, r.date,0)
//                val freeEndTimeList = mutableListOf<Time>()
//                val freStartTimeList = mutableListOf<Time>()
//                for (i in freeCourtTime){
//                    freeEndTimeList.add(i.endTime)
//                    freStartTimeList.add(i.startTime)
//                }
//



                    }
            }
        }

        fun setSelDate(newDate: LocalDate) {
            _selDate.value = newDate
        }

        fun setStartTime(newST: Time) {
            _selStartTime.value = newST
        }



        fun setCourtName(newCourtName: String) {
            _selCourtName.value = newCourtName
        }

        fun setSport(newSport: String) {
            _selSport.value = newSport
        }

        fun getCourtNames()  {

            db1.collection("court").get()
                .addOnSuccessListener { querySnapshot->
                    val test = querySnapshot.documents.map {
                        documentSnapshot ->documentSnapshot.id
                    }
                    _courtNames.value = test

                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }

            //  val courtNameList = courtNameIdMap.keys.toTypedArray()



        }

        fun getFreeSlotBySportAndDate(sport: String, date: LocalDate, application: Application) {
            db = AppDatabase.getDatabase(application)
            _freeSlots.value = db.courtDao().getFreeSlotBySportAndDate(sport, date)
        }
        fun getFreeSlotByCourtName(courtName:String){
//            val freeEndTimeList = mutableListOf<Time>()
            val freeStartTimeList = mutableListOf<Time>()
            db1.collection("court").document(courtName).collection("courtTime")
                .document("${selDate.value}").get()
                .addOnSuccessListener { result ->
                    if (result != null) {
                        result.data?.forEach { ct ->
                            if (ct.value == true) {

                                freeStartTimeList.add(Time(ct.key.toInt(), 0,0))
                                //freeEndTimeList.add(Time(ct.key.toInt() + 1, 0,0))
                            }

                        }
                        if (_selectedRes.value!!.name == selCourtName.value && selDate.value == _selectedRes.value!!.date){
                            freeStartTimeList.add(_selectedRes.value!!.startTime)
                        }

                        _freeStartTimes.value = freeStartTimeList

                    } else {
                        Log.d(TAG, "No such document")
                    }
                }  .addOnFailureListener { exception ->
                    // 处理错误
                    Log.d(TAG,"Error getting documents: ${exception.message}")

                }
        }
        fun getAllFreeSLotByCourtIdAndDate(
            courtId: Int,
            date: LocalDate,
            status: Int,
            application: Application
        ) {
            val freeCourtTime = AppDatabase.getDatabase(application).courtDao()
                .getAllFreeSlotByCourtIdAndDate(courtId, date, status)
            val freeEndTimeList = mutableListOf<Time>()
            val freStartTimeList = mutableListOf<Time>()
            for (i in freeCourtTime) {
                freeEndTimeList.add(i.endTime)
                freStartTimeList.add(i.startTime)
            }
//            if (_selectedRes.value!!.startTime == selDate) freStartTimeList.add(_selectedRes.value!!.startTime)
//            if (_selectedRes.value!!.endTime == selDate) freeEndTimeList.add(_selectedRes.value!!.endTime)


            _freeStartTimes.value = freStartTimeList
        }

        fun addOrUpdateRes(userId: Int,rating: Int) {


            var startTimeDay = Timestamp(Date(selDate.value!!.year - 1900, selDate.value!!.monthValue -1, selDate.value!!.dayOfMonth, selStartTime.value!!.hours, selStartTime.value!!.minutes))
            var endTimeDay = Timestamp(Date(selDate.value!!.year - 1900, selDate.value!!.monthValue-1, selDate.value!!.dayOfMonth, selStartTime.value!!.hours + 1, selStartTime.value!!.minutes))

            val newRes = hashMapOf(

                "ct" to mapOf(
                    "endTime" to endTimeDay,
                    "startTime" to startTimeDay
                ),
                "description" to selectedRes.value!!.description,
                "name" to "${selCourtName.value}",
                "rating" to rating,
                "review" to selectedRes.value!!.review,
                "sport" to selSport.value,
                "status" to 0

            )
            println(newRes)
            db1.collection("users").document("u$userId").collection("reservation").document(selectedRes.value!!.resId)
                .set(newRes)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        }

        fun deleteRes(userid: Int, resId:String) {


            db1.collection("users").document("u$userid").collection("reservation").document("$resId")
                .update("status",1).addOnSuccessListener{
                    Log.d(TAG, "delete successfully")
                }


        }

        fun clear() {
            _reservations.value = null
        }



}