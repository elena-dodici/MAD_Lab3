package com.example.lab3

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.entity.MyReservation
import com.example.lab3.database.entity.Review
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Time
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class r(
    var rating: Int,
    var review: String,
    var user: String
)

class HistoryViewModel : ViewModel() {
    val TAG = "HistoryVM"
    val db1 = Firebase.firestore
    private var _reservations =
        MutableLiveData<List<MyReservation>>().also { listOf<MyReservation>() }
    val reservations: LiveData<List<MyReservation>> = _reservations

    private var _selRev = MutableLiveData<Review>()
    val selRev: LiveData<Review> = _selRev

    private var _originRating = 0

    fun getHistoryResbyUser(userid: String) { // 只拿今天之前的！
        db1.collection("users").document("u${userid}").collection("reservation").get()
            .addOnSuccessListener { result ->
                val dataList = result.map { document ->
                    val mapId = mapOf(
                        "resId" to document.id,
                    )
                    mapId + document.data
                }
                val myres = mutableListOf<MyReservation>()
                dataList.forEach { res -> // 遍历每一个reservation

                    val ct = res["ct"] as Map<*, *>
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

                    if (res["status"].toString() == "0" && startTime.toLocalDate() < LocalDate.now()) {
                        Log.d(TAG, "show reservaton ${startTime.toLocalDate()} ")
                        myres.add(

                            MyReservation(
                                res["resId"].toString(),
                                res["name"].toString(),
                                res["sport"].toString(),
                                Time(startTime.hour, startTime.minute, startTime.second),
                                Time(endTime.hour, endTime.minute, endTime.second),
                                startTime.toLocalDate(), res["description"].toString(),
                                res["review"].toString(),
                                res["rating"].toString().toInt()
                            )
                        )
                    }

                }
                _reservations.value = myres
                Log.d(TAG, "history reservaton ${_reservations.value!!} ")
            }
            .addOnFailureListener { exception ->
                // 处理错误
                println("Error getting documents: ${exception.message}")
                _reservations.value = listOf()
            }
    }

    fun setHisReview(resId: String) {
        for (r in _reservations.value!!) {
            if (r.resId == resId) {
                _selRev.value = Review(r.resId, r.rating, r.review, r.name)
                _originRating = _selRev.value!!.rating

            }
        }
    }

    fun addRev(rating: Int, userId: String) {
        val docRef = db1.collection("users").document("u${userId}").collection("reservation")
            .document("${selRev.value!!.resId}")
        docRef.update("rating", rating, "review", _selRev.value!!.review).addOnSuccessListener {
            Log.d(TAG, "review ${_selRev.value!!.review} with $rating star update ok")

        }.addOnFailureListener { exception ->
            // 处理错误
            Log.d(TAG, "Error getting documents: ${exception.message}")

        }

        var newAvgrating: Float
        db1.collection("court").document("${_selRev.value!!.name}")
            .get()
            .addOnSuccessListener { docs ->

                var reviewList = docs.data?.get("review") as? List<*>
                var sumRating = 0

                val numOfRev = reviewList!!.size

                reviewList.forEach { r ->

                    val mapItem = r as Map<String, Any?>
                    sumRating += mapItem["rating"].toString().toInt()

                }
                //new review
                newAvgrating = if (_selRev.value!!.rating < 0) {
                    (sumRating + rating).toFloat() / (numOfRev + 1)
                } else {
                    (sumRating - _selRev.value!!.rating + rating).toFloat() / numOfRev
                }

                //update new avg_rating
                var docRef = db1.collection("court").document("${_selRev.value!!.name}")
                docRef.update("avg_rating", newAvgrating)
                    .addOnSuccessListener {
                        Log.d(
                            TAG,
                            "new avg_rat = ${newAvgrating} successfully written!"
                        )
                    }
                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }


                docRef.get().addOnSuccessListener { result ->
                    if (result != null) {
                        var reviewList = result.data?.get("review") as? MutableList<*>
                        val newList = mutableListOf<r>()
                        if (reviewList != null) {
                            if (reviewList.size > 0) {
                                for (i in reviewList) {
                                    val mapItem = (i as MutableMap<String, Any?>).toMutableMap()
                                    if (mapItem["user"] == "u$userId") {
                                        mapItem["rating"] = rating
                                        mapItem["review"] = _selRev.value!!.review
                                    }
                                    newList.add(
                                        r(
                                            mapItem["rating"].toString().toInt(),
                                            mapItem["review"].toString(),
                                            mapItem["user"].toString()
                                        )
                                    )

                                }
                                //update review list
                                docRef.update("review", newList)
                                    .addOnSuccessListener {
                                        Log.d(
                                            TAG,
                                            "review list successfully updated!"
                                        )
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(
                                            TAG,
                                            "Error update document",
                                            e
                                        )
                                    }


                            }

                        }
                    }

                }


            }
    }
}