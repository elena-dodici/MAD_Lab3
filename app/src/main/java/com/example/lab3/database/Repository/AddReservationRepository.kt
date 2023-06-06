package com.example.lab3.database.Repository

import com.example.lab3.database.entity.ReservationFirebase
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate

class AddReservationRepository {
    val db1 = Firebase.firestore

    suspend fun updateTimeSlot(sport : String, date : String?, timeSlot : String) : Int =
        withContext(Dispatchers.IO){
            lateinit var courtName : String
            when(sport){
                "running" -> courtName = "court1"
                "basketball" -> courtName = "court2"
                "swimming" -> courtName = "court3"
                "pingpong" -> courtName = "court4"
                "tennis" -> courtName = "court5"
            }
           val query = db1.collection("court")
                .document(courtName)
                .collection("courtTime")
                .document(date!!)
                .update("${timeSlot}",false)
                .addOnSuccessListener {
                    println("Data updated successfully")
                }
                .addOnFailureListener {
                    println("Error")
                }
            try {
                query.await()
                return@withContext 1
            } catch (error : Throwable){
                println(error)
                return@withContext -1
            }
        }

    suspend fun getNumberOfUserReservations(user : Int) : Int =
        withContext(Dispatchers.IO) {
            val query = db1.collection("users")
                .document("u${user}")
                .collection("reservation").count()
                .get(AggregateSource.SERVER)
                .addOnSuccessListener { res ->
                    val numberOfRes = res.count.toInt()
                }
                .addOnFailureListener { exception ->
                    println("Error : ${exception}")
                }
            try {
                val result = query.await().count.toInt()
                return@withContext result
            }catch (error : Throwable){
                println("Error : ${error}")
                return@withContext -1
            }
        }

    suspend fun addNewReservation(reservation : ReservationFirebase, user : Int, numberOfRes : Int) : Int =
        withContext(Dispatchers.IO) {
            val res = hashMapOf(
                "ct" to mapOf(
                    "endTime" to reservation.endTime,
                    "startTime" to reservation.startTime
                ),
                "description" to reservation.description,
                "name" to reservation.court,
                "rating" to reservation.rating,
                "review" to reservation.review,
                "sport" to reservation.sport,
                "status" to reservation.status
            )
            val query = db1.collection("users")
                .document("u${user.toString()}")
                .collection("reservation")
                .document("res${numberOfRes.toString()}")
                .set(res)
                .addOnSuccessListener {
                    println("Data added successfully")
                }
                .addOnFailureListener {
                        exception ->
                    println("Error : ${exception}")
                }
            try {
                val result = query.await()
                println("ADDING res${numberOfRes} for u${user}")
                return@withContext 1
            }catch (error : Throwable){
                println("Error : ${error}")
                return@withContext -1
            }
        }

    fun addResWithTransaction(reservation: ReservationFirebase, user: Int, sport: String, date: String?, timeSlot: String, numberOfRes: Int) {
        lateinit var courtName: String

        db1.runTransaction { transaction ->
            try {
                when (sport) {
                    "running" -> courtName = "court1"
                    "basketball" -> courtName = "court2"
                    "swimming" -> courtName = "court3"
                    "pingpong" -> courtName = "court4"
                    "tennis" -> courtName = "court5"
                }

                transaction.update(
                    db1.collection("court")
                        .document(courtName)
                        .collection("courtTime")
                        .document(date!!)
                    , timeSlot, false)

                val res = hashMapOf(
                    "ct" to mapOf(
                        "endTime" to reservation.endTime,
                        "startTime" to reservation.startTime
                    ),
                    "description" to reservation.description,
                    "name" to reservation.court,
                    "rating" to reservation.rating,
                    "review" to reservation.review,
                    "sport" to reservation.sport,
                    "status" to reservation.status
                )

                transaction.set(
                    db1.collection("users")
                        .document("u$user")
                        .collection("reservation")
                        .document("res$numberOfRes")
                    , res)

                null // Return null to indicate success
            } catch (e: Exception) {
                throw FirebaseFirestoreException("Error: $e", FirebaseFirestoreException.Code.ABORTED)
            }
        }.addOnSuccessListener { result ->
            println("Transaction success: $result")
        }.addOnFailureListener { e ->
            println("Transaction failure: $e")
        }
    }
    }
