package com.example.lab3

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.MyReservation
import com.example.lab3.database.entity.FreeCourt
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Time
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class  CalendarViewModel() : ViewModel( ) {
    //reservationList of this user
    private var _reservations = MutableLiveData<List<MyReservation>>()
    val reservations:LiveData<List<MyReservation>> = _reservations
    //allCourt
    private var _courts = MutableLiveData<List<Court>>().also { it.value = listOf() }
    val courts:LiveData<List<Court>> = _courts
    lateinit var db: AppDatabase
    //reservationId
    private var _resIdvm = MutableLiveData<Int>()
    val resIdvm : LiveData<Int> = _resIdvm

    //selected Reservation
    private var _selectedRes = MutableLiveData<MyReservation>()
    val selectedRes: LiveData<MyReservation> = _selectedRes



    //selected Date
    private var _selDate = MutableLiveData<LocalDate>()
    val selDate : LiveData<LocalDate> = _selDate

    private var _selStartTime = MutableLiveData<Time>()
    val selStartTime : LiveData<Time> = _selStartTime
    private var _selEndTime = MutableLiveData<Time>()
    val selEndTime : LiveData<Time> = _selEndTime

    private var _selDes = MutableLiveData<String>()
    val selDes : LiveData<String> = _selDes

    private var _freeEndTimes = MutableLiveData<List<Time>>().also { it.value = listOf() }
    val freeEndTimes: LiveData<List<Time>> = _freeEndTimes
    private var _freeStartTimes = MutableLiveData<List<Time>>().also { it.value = listOf() }
    val freeStartTimes:LiveData<List<Time>> = _freeStartTimes

    private var _sportList = listOf("running", "basketball", "swimming","tennis","pingpong")
    val sportList = _sportList

    private var _freeSlots = MutableLiveData<List<FreeCourt>>().also { it.value = listOf() }
    val freeSlots = _freeSlots

    private var _F = MutableLiveData<List<FreeCourt>>().also { it.value = listOf() }
    val F:LiveData<List<FreeCourt>> = _F

    private var _courtNames = MutableLiveData<List<String>>().also { it.value = listOf() }
    val courtNames:LiveData<List<String>> = _courtNames

    val db1 = Firebase.firestore



    private var _selCourtId = MutableLiveData<Int>()
    val selCourtId : LiveData<Int> = _selCourtId

    private var _selSport = MutableLiveData<String>()
    val selSport : LiveData<String> = _selSport


    fun getAllRes(application: Application, userid:Int){
//        db = AppDatabase.getDatabase(application)

        db1.collection("users").document("u${userid}").collection("reservation").get()
            .addOnSuccessListener { result ->
                val dataList = result.map { document ->
                    document.data
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

                    val zone = ZoneId.of("UTC+1")
//                    val zone = ZoneId.systemDefault()
                    val startTime = Instant.ofEpochSecond(secondsStart!!).atZone(zone)

                    matchResult = regex.find(endtimeStr)
                    val secondsEnd = matchResult?.groupValues?.get(1)?.toLongOrNull()
//                    val endtimestamp = seconds?.let { Timestamp(it,0) }
                    val endTime = Instant.ofEpochSecond(secondsEnd!!).atZone(zone)

//                    println(res["description"].toString())
                    if (res["status"].toString() == "0"){
                        myres.add(
                            MyReservation(res["name"].toString(), res["sport"].toString(),Time(startTime.hour,startTime.minute,startTime.second), Time(endTime.hour,endTime.minute,endTime.second), startTime.toLocalDate(),res["description"].toString() )
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
    fun getCourts(application: Application){
        db = AppDatabase.getDatabase(application)
        _courts.value = db.courtDao().getAllCourts()
    }

    private var _FreeSlotsOneDay = MutableLiveData<MutableMap<String,Map<String,Boolean>>>()
    val FreeSlotsOneDay:LiveData<MutableMap<String,Map<String,Boolean>>> = _FreeSlotsOneDay
    private var _FullDate = MutableLiveData<MutableMap<LocalDate,Boolean>>()
    val FullDate:LiveData<MutableMap<LocalDate,Boolean>> = _FullDate
    private var _allDate = MutableLiveData<MutableList<LocalDate>>()
    val allDate:LiveData<MutableList<LocalDate>> = _allDate


    fun getDateFull(application: Application,sport:String,ad:List<LocalDate>) {
        var fd = mutableMapOf<LocalDate, Boolean>()
        ad.forEach {
            fd[it]=false
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
                                fd[localDate]=true
                            }
                        }
                        _FullDate.value = fd
                    }
                }
            }
    }
    fun getAllDate(application: Application){
        val ad = mutableListOf<LocalDate>()
        db1.collection("court").document("court1").collection("courtTime")
            .get().addOnSuccessListener {
                it.forEach{DATE->
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val localDate = LocalDate.parse(DATE.id, formatter)
                    ad.add(localDate)
                }
                _allDate.value=ad
            }
    }
    fun updateAllDate(application: Application){
        val ad = mutableListOf<LocalDate>()
        db1.collection("court").document()
            .get().addOnSuccessListener {
                println("court"+ it.id)
                it.reference.collection("courtTime")
            }
    }

    fun getFreeSlotByDateAndSport(application: Application, sport:String, date: LocalDate?){
        //ct的key为court的名字，value为某一天所有时间段和对应占用状态
        var ct = mutableMapOf<String,Map<String,Boolean>>()
        //搜索所有运动为sport的court
        db1.collection("court").whereEqualTo("sport","${sport}")
            .get().addOnSuccessListener {
                it.forEach{court->
                    val sportName =court.id.toString()
                    val allFreeSlot = court.reference.collection("courtTime")
                    allFreeSlot.get().addOnSuccessListener {courtTimeQuerySnapshot->
                        courtTimeQuerySnapshot.forEach{slot->
                            if (slot.id==date.toString()){

                                if (sportName != null) {
                                    ct.put(sportName, slot.data as Map<String, Boolean>)
                                }

                            }
                        }
                        _FreeSlotsOneDay.value=ct
                    }
                }
            }.addOnFailureListener { exception ->
                // 处理错误
                println("Error getting documents: ${exception.message}")
            }
    }


    fun getAllCourtTime(application: Application,sport:String){
        db = AppDatabase.getDatabase(application)
        _F.value =db.courtTimeDao().getAllTCourtTimes(sport)
    }



    fun setSelectedResByResId(resId:Int, application:Application){
//        for (r in _reservations.value!! ){
//            if (r.resId == resId) {
//                _selectedRes.value = r
//                _selDate.value = r.date
//                _resIdvm.value = resId
//                _selCourtId.value = r.courtId
//                _selStartTime.value = r.startTime
//                _selEndTime.value = r.endTime
//                _selDes.value = r.description
//                _selSport.value = r.sport
//                //getAllFreeSLotByCourtIdAndDate(r.courtId, r.date,0,application)
//                val freeCourtTime = AppDatabase.getDatabase(application).courtDao().getAllFreeSlotByCourtIdAndDate(r.courtId, r.date,0)
//                val freeEndTimeList = mutableListOf<Time>()
//                val freStartTimeList = mutableListOf<Time>()
//                for (i in freeCourtTime){
//                    freeEndTimeList.add(i.endTime)
//                    freStartTimeList.add(i.startTime)
//                }
//
//                freeEndTimeList.add(_selectedRes.value!!.endTime)
//                freStartTimeList.add(_selectedRes.value!!.startTime)
//                _freeEndTimes.value  = freeEndTimeList
//                _freeStartTimes.value = freStartTimeList
//            }
//        }
    }

    fun setSelDate(newDate: LocalDate){
        _selDate.value = newDate
    }

    fun setStartTime(newST: Time){
        _selStartTime.value = newST
    }
    fun setEndTime(newET: Time){
        _selEndTime.value = newET
    }

    fun setCourtId(newCourtId: Int){
        _selCourtId.value= newCourtId
    }

    fun setSport(newSport: String){
        _selSport.value= newSport
    }

    fun getCourtNameIdBySport(sport: String, application: Application) :  Map<String,Int>{
        db = AppDatabase.getDatabase(application)

        val courts =  db.courtDao().getCourtBySport(sport)
        val courtNameIdMap =  mutableMapOf<String,Int>()
        for (i in courts){
            courtNameIdMap[i.name] = i.courtId
        }

        //  val courtNameList = courtNameIdMap.keys.toTypedArray()
        return courtNameIdMap

    }

    fun getFreeSlotBySportAndDate(sport: String, date: LocalDate, application: Application){
        db = AppDatabase.getDatabase(application)
        _freeSlots.value = db.courtDao().getFreeSlotBySportAndDate(sport, date)
    }
     fun getAllFreeSLotByCourtIdAndDate(courtId : Int, date:LocalDate, status:Int, application:Application){
         val freeCourtTime = AppDatabase.getDatabase(application).courtDao().getAllFreeSlotByCourtIdAndDate(courtId, date,status)
         val freeEndTimeList = mutableListOf<Time>()
         val freStartTimeList = mutableListOf<Time>()
         for (i in freeCourtTime){
             freeEndTimeList.add(i.endTime)
             freStartTimeList.add(i.startTime)
         }
        if (_selectedRes.value!!.startTime== selDate) freStartTimeList.add(_selectedRes.value!!.startTime)
         if (_selectedRes.value!!.endTime== selDate) freeEndTimeList.add(_selectedRes.value!!.endTime)

         _freeEndTimes.value  = freeEndTimeList
         _freeStartTimes.value = freStartTimeList
    }

    fun addOrUpdateRes(application: Application){
        //find courtTImdId
//        val newCourtTime =  AppDatabase.getDatabase(application).courtTimeDao().getCourtTimeId(_selStartTime.value!!,_selEndTime.value!!,selCourtId.value!!)
//        if (newCourtTime != null) {
//            _selectedRes.value!!.courtTimeId = newCourtTime.id
//        }
//
//        val updateRes = Reservation(_selectedRes.value!!.courtTimeId, 1, 0,  _selDate.value!!,_selectedRes.value!!.description)
//        updateRes.resId = _selectedRes.value!!.resId
//        AppDatabase.getDatabase(application).reservationDao().addReservation(updateRes)

    }

    fun deleteRes(resId: Int, application: Application){
        AppDatabase.getDatabase(application).reservationDao().deleteReservation(resId)
    }

    fun clear(){
        _reservations.value = listOf()
    }


}