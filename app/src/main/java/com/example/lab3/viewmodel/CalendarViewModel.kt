package com.example.lab3

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.FreeCourt
import com.example.lab3.database.entity.MyReservation
import com.google.firebase.Timestamp
import com.google.firebase.Timestamp.now
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Time
import java.time.Instant

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date




class CalendarViewModel : ViewModel() {
    private val TAG = "CalendarVM"

    //reservationList of this user

    private var _reservations = MutableLiveData<List<MyReservation>>()
    val reservations:LiveData<List<MyReservation>> = _reservations

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

    private var _selRating = MutableLiveData<Int>()
    val selRating: LiveData<Int> = _selRating

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
/*
    private var _courtNames = MutableLiveData<List<String>>().also { it.value = listOf() }
    val courtNames: LiveData<List<String>> = _courtNames*/

    val db1 = Firebase.firestore


    private var _selCourtName = MutableLiveData<String>()
    val selCourtName: LiveData<String> = _selCourtName

    private var _selSport = MutableLiveData<String>()
    val selSport: LiveData<String> = _selSport




    fun test(userid: Int) {
        val colRef = db1.collection("users").document("u${userid}")
        colRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            getAllRes(userid)

        }
    }


    fun getAllRes(userid:Int){
//        db = AppDatabase.getDatabase(application)

        db1.collection("users").document("u${userid}").collection("reservation").get()
            .addOnSuccessListener { result ->
                val dataList = result.map { document ->
                    val mapId = mapOf(
                        "resId" to document.id,
                    )
                    mapId + document.data
                }
                val myres = mutableListOf<MyReservation>()
                dataList.forEach{res-> // 遍历每一个reservation
//                    println(res["name"].toString())
                    val ct = res["ct"] as Map<*,*>
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
                    if (res["status"].toString() == "0"){
                        myres.add(
                            MyReservation(res["resId"].toString(),res["name"].toString(), res["sport"].toString(),Time(startTime.hour,startTime.minute,startTime.second), Time(endTime.hour,endTime.minute,endTime.second), startTime.toLocalDate(),res["description"].toString(),res["review"].toString(),res["rating"].toString().toInt() )
                        )
                    }

                }
                _reservations.value = myres
//                _reservations.value = dataList
            }
            .addOnFailureListener { exception ->
                // 处理错误
                println("Error getting documents: ${exception.message}")
                _reservations.value = listOf()
            }
//        _reservations.value = db.reservationDao().getReservationByUserId(userid)
//        getCourts(application)
    }




    private var _FreeSlotsOneDay = MutableLiveData<MutableMap<String, Map<String, Boolean>>>()
    val FreeSlotsOneDay: LiveData<MutableMap<String, Map<String, Boolean>>> = _FreeSlotsOneDay
    private var _FullDate = MutableLiveData<MutableMap<LocalDate, Boolean>>()
    val FullDate: LiveData<MutableMap<LocalDate, Boolean>> = _FullDate
    private var _allDate = MutableLiveData<MutableList<LocalDate>>()

    val allDate:LiveData<MutableList<LocalDate>> = _allDate



    fun getDateFull(application: Application, sport: String, ad: List<LocalDate>) {
        var fd = mutableMapOf<LocalDate, Boolean>()
        ad.forEach {
            fd[it] = false
        }
        db1.collection("court").whereEqualTo("sport", "${sport}")
            .get().addOnSuccessListener {
                it.forEach { court ->
                    val allFreeSlot = court.reference.collection("courtTime")
                    allFreeSlot.get().addOnSuccessListener { courtTimeQuerySnapshot ->
                        courtTimeQuerySnapshot.forEach { date ->
                            if (date.data.containsValue(true)) {
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                val localDate = LocalDate.parse(date.id, formatter)
                                fd[localDate] = true
                            }
                        }
                        _FullDate.value = fd
                    }
                }
            }
    }

    fun getAllDate(application: Application) {
        val ad = mutableListOf<LocalDate>()
        db1.collection("court").document("court1").collection("courtTime")
            .get().addOnSuccessListener {
                it.forEach { DATE ->
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val localDate = LocalDate.parse(DATE.id, formatter)
                    ad.add(localDate)
                }
                _allDate.value = ad
            }
    }

    fun updateAllDate(application: Application) {
        val ad = mutableListOf<LocalDate>()
        db1.collection("court").document()
            .get().addOnSuccessListener {
                println("court" + it.id)
                it.reference.collection("courtTime")
            }
    }

    fun getFreeSlotByDateAndSport(application: Application, sport: String, date: LocalDate?) {
        //ct的key为court的名字，value为某一天所有时间段和对应占用状态
        var ct = mutableMapOf<String, Map<String, Boolean>>()
        //搜索所有运动为sport的court
        db1.collection("court").whereEqualTo("sport", "${sport}")
            .get().addOnSuccessListener {
                it.forEach { court ->
                    val sportName = court.id.toString()
                    val allFreeSlot = court.reference.collection("courtTime")
                    allFreeSlot.get().addOnSuccessListener { courtTimeQuerySnapshot ->
                        courtTimeQuerySnapshot.forEach { slot ->
                            if (slot.id == date.toString()) {

                                if (sportName != null) {
                                    ct.put(sportName, slot.data as Map<String, Boolean>)
                                }

                            }
                        }
                        _FreeSlotsOneDay.value = ct
                    }
                }
            }.addOnFailureListener { exception ->
                // 处理错误
                println("Error getting documents: ${exception.message}")
            }
    }





    fun setSelectedResByCourtName(resId: String) {

//        getCourtNamesAndSport()
        var freeStartTimeList = mutableListOf<Time>()
        val freeEndTimeList = mutableListOf<Time>()
        for (r in _reservations.value!!) {
            if (r.resId == resId) {
                _selectedRes.value = r
                _selDate.value = r.date
                _selStartTime.value = r.startTime
                _selCourtName.value = r.name
                _selRating.value = r.rating
                _selSport.value = r.sport

                db1.collection("court").document(r.name).collection("courtTime")
                    .document("${r.date}").get()
                    .addOnSuccessListener { result ->
                        if (result != null) {
                            result.data?.forEach { ct ->
                                if (ct.value == true) {

                                    freeStartTimeList.add(Time(ct.key.toInt(), 0, 0))
                                    freeEndTimeList.add(Time(ct.key.toInt() + 1, 0, 0))
                                }

                            }

                            freeStartTimeList.add(_selectedRes.value!!.startTime)
                            Log.d(TAG, "freeStartTimeList in setselected is : ${freeStartTimeList}")
                            _freeStartTimes.value = freeStartTimeList

                        } else {
                            Log.d(TAG, "No such document")
                        }
                    }.addOnFailureListener { exception ->
                        // 处理错误
                        Log.d(TAG, "Error getting documents: ${exception.message}")

                    }




            }
        }
    }

    fun setSelDate(newDate: LocalDate) {
        _selDate.value = newDate
    }
    fun setSelSport(newSport: String) {
        _selSport.value = newSport
    }

    fun setStartTime(newST: Time) {
        _selStartTime.value = newST
    }


    fun setSelCourtName(newCourtName: String) {

        _selCourtName.value = newCourtName
    }
    var courtNamesSport = hashMapOf<String,String>()
    fun getCourtNamesAndSport() {

        db1.collection("court").get()
            .addOnSuccessListener { querySnapshot ->
                val test = querySnapshot.map { documentSnapshot ->
                    val mapId = mapOf(
                        "cn" to documentSnapshot.id,
                    )
                    mapId + documentSnapshot.data
                }
                test.forEach{res->


                    courtNamesSport.put(res["cn"].toString(),res["sport"].toString())
                }


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

    fun getFreeSlotByCourtName(courtName: String) {

        val freeStartTimeList = mutableListOf<Time>()
        db1.collection("court").document(courtName).collection("courtTime")
            .document("${selDate.value}").get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    result.data?.forEach { ct ->
                        if (ct.value == true) {

                            freeStartTimeList.add(Time(ct.key.toInt(), 0, 0))
                            //freeEndTimeList.add(Time(ct.key.toInt() + 1, 0,0))
                        }

                    }
                    if (_selectedRes.value!!.name == selCourtName.value && selDate.value == _selectedRes.value!!.date) {
                        freeStartTimeList.add(_selectedRes.value!!.startTime)
                    }

                    _freeStartTimes.value = freeStartTimeList

                } else {
                    Log.d(TAG, "No such document")
                }
            }.addOnFailureListener { exception ->
                // 处理错误
                Log.d(TAG, "Error getting documents: ${exception.message}")

            }
    }


    fun addOrUpdateRes(userId: Int) {


        var startTimeDay = Timestamp(
            Date(
                selDate.value!!.year - 1900,
                selDate.value!!.monthValue - 1,
                selDate.value!!.dayOfMonth,
                selStartTime.value!!.hours,
                selStartTime.value!!.minutes
            )
        )
        var endTimeDay = Timestamp(
            Date(
                selDate.value!!.year - 1900,
                selDate.value!!.monthValue - 1,
                selDate.value!!.dayOfMonth,
                selStartTime.value!!.hours + 1,
                selStartTime.value!!.minutes
            )
        )


        val newRes = hashMapOf(

            "ct" to mapOf(
                "endTime" to endTimeDay,
                "startTime" to startTimeDay
            ),
            "description" to selectedRes.value!!.description,
            "name" to "${selCourtName.value}",
            "rating" to selectedRes.value!!.rating,
            "review" to selectedRes.value!!.review,
            "sport" to selSport.value,
            "status" to 0

        )

                //set new reservation
        db1.collection("users").document("u$userId").collection("reservation")
            .document(selectedRes.value!!.resId)
            .set(newRes)
            .addOnSuccessListener {

                Log.d(TAG, "new reservtion add successfully written!")


            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

//                db1.collection("users").document("u$userId").collection("reservation")
//                    .document(selectedRes.value!!.resId)
//                    .get().addOnSuccessListener { result ->
//                        result.data
//
//                    }




                //update freeslot time
                //changeinto false
                val docRef1 = db1.collection("court").document(selCourtName.value!!).collection("courtTime").document("${selDate.value}")
                docRef1.get().addOnSuccessListener { result ->
                         docRef1.update("${selStartTime.value!!.hours}", false)
                            .addOnSuccessListener {
                                Log.d(TAG, "${selDate.value} of ${selCourtName.value} at this time  ${selStartTime.value!!.hours} set to false successfully!")
                            }
                            .addOnFailureListener { exception ->
                                Log.e("TAG", "Error updating field value: $exception")
                            }

                    }.addOnFailureListener { exception -> Log.d(TAG, " ${exception.message}") }
                //change into true
                val docRef2 =  db1.collection("court").document(selectedRes.value!!.name).collection("courtTime").document("${selectedRes.value!!.date}")
                docRef2.get().addOnSuccessListener { result ->
                    docRef2.update("${selectedRes.value!!.startTime.hours}", true)
                        .addOnSuccessListener {
                            Log.d(TAG, "${selectedRes.value!!.date} of ${selectedRes.value!!.name} at this time  ${selectedRes.value!!.startTime.hours} set to true successfully!")
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Error updating field value: $exception")
                        }

                    }.addOnFailureListener { exception -> Log.d(TAG, " ${exception.message}") }

                Log.d(TAG, "DocumentSnapshot successfully written!")
//            }
//            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    fun deleteRes(userid: Int, resId: String) {


        db1.collection("users").document("u$userid").collection("reservation").document("$resId")
            .update("status", 1).addOnSuccessListener {

                Log.d(TAG, "delete successfully")
            }

        //update freeslot time
        //changeinto false

        //change into true
        val docRef2 =  db1.collection("court").document(selCourtName.value!!).collection("courtTime").document("${selectedRes.value!!.date}")
        docRef2.get().addOnSuccessListener { result ->
            docRef2.update("${selectedRes.value!!.startTime.hours}", true)
                .addOnSuccessListener {
                    Log.d(TAG, "${selectedRes.value!!.date} of ${selCourtName.value} at this time  ${selectedRes.value!!.startTime.hours} set to true successfully!")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Error updating field value: $exception")
                }

        }.addOnFailureListener { exception -> Log.d(TAG, " ${exception.message}") }


    }



    fun clear(){
        _reservations.value = listOf()

    }


}