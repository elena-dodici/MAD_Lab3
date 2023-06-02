package com.example.lab3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.entity.MyReservation
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Time
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class HistoryViewModel:ViewModel() {
    private var _reservations = MutableLiveData<List<MyReservation>>().also { listOf<MyReservation>() }
    val reservations: LiveData<List<MyReservation>> = _reservations
    val db1 = Firebase.firestore

    fun getHistoryResbyUser(userid:Int){ // 只拿今天之前的！
        db1.collection("users").document("u${userid}").collection("reservation").get()
            .addOnSuccessListener { result ->
                val dataList = result.map { document ->
                    document.data
                }
                val myres = mutableListOf<MyReservation>()
                dataList.forEach{res-> // 遍历每一个reservation
                    val ct = res["ct"] as Map<*,*>
                    val starttimeStr = ct["startTime"].toString()
                    val endtimeStr = ct["endTime"].toString()
                    // 使用正则表达式提取秒数值
                    val regex = "seconds=(\\d+)".toRegex()
                    var matchResult = regex.find(starttimeStr)
                    val secondsStart = matchResult?.groupValues?.get(1)?.toLongOrNull()

                    val zone = ZoneId.of("UTC+2")
                    val startTime = Instant.ofEpochSecond(secondsStart!!).atZone(zone)

                    matchResult = regex.find(endtimeStr)
                    val secondsEnd = matchResult?.groupValues?.get(1)?.toLongOrNull()

                    val endTime = Instant.ofEpochSecond(secondsEnd!!).atZone(zone)

                    if (res["status"].toString() == "0" && startTime.toLocalDate()< LocalDate.now()){
                        myres.add(
                            MyReservation(res["name"].toString(), res["sport"].toString(),
                                Time(startTime.hour,startTime.minute,startTime.second), Time(endTime.hour,endTime.minute,endTime.second), startTime.toLocalDate(),res["description"].toString() )
                        )
                    }

                }
                _reservations.value = myres
            }
            .addOnFailureListener { exception ->
                // 处理错误
                println("Error getting documents: ${exception.message}")
                _reservations.value = listOf()
            }
    }
}