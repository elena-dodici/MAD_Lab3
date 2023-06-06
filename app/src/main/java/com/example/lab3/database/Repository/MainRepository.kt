package com.example.lab3.database.Repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainRepository {
    val db1 = Firebase.firestore

    suspend fun getLastDayInCourtTimes() : String =
        withContext(Dispatchers.IO) {
            val query = db1.collection("court").document("court1").collection("courtTime")
                .get()
                .addOnSuccessListener {
                        documents ->
                    val lastDate = documents.documents.last().id
                }
                .addOnFailureListener { exception ->
                    println("Error : ${exception}")
                }
            try {
                val result = query.await().last().id
                return@withContext result
            }catch (error : Throwable){
                println("Error : ${error}")
                return@withContext "NA"
            }
        }

    suspend fun addDayInDatabase(day : String, court : String){
        withContext(Dispatchers.IO) {
            val timeSlots = hashMapOf(
               "9" to true,
                "10" to true,
                "11" to true,
                "12" to true,
                "13" to true,
                "14" to true,
                "15" to true,
                "16" to true,
                "17" to true,
                "18" to true,
                "19" to true,
            )
            val query = db1.collection("court")
                .document("${court}")
                .collection("courtTime")
                .document("${day}")
                .set(timeSlots)
                .addOnSuccessListener {
//                    println("Data added successfully")
                }
                .addOnFailureListener {
                        exception ->
                    println("Error : ${exception}")
                }
            try {
                val result = query.await()
//                println("ADDING day${day}")
                return@withContext 1
            }catch (error : Throwable){
                println("Error : ${error}")
                return@withContext -1
            }
        }
    }
}