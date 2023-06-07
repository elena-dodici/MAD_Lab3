package com.example.lab3.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.Contact
import com.example.lab3.database.entity.MyReservation
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Time
import java.time.Instant
import java.time.ZoneId

class ContactViewModel: ViewModel() {
    private val TAG = "ContactVM"

    private var _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> = _contacts
    val db1 = Firebase.firestore

    fun getAllContact(uuid:String){
        db1.collection("users").document("u$uuid").collection("contact").get()

            .addOnSuccessListener { result ->
                val dataList = result.map { document ->
                    val mapId = mapOf(
                        "uid" to document.id,
                    )
                    mapId + document.data
                }
                val myContact = mutableListOf<Contact>()
                dataList.forEach{res-> // 遍历每一个contact
                    myContact.add(
                        Contact(res["uid"].toString() ,res["name"].toString(), res["status"].toString().toInt())
                    )
                }
                _contacts.value = myContact
//                _reservations.value = dataList
                Log.d(TAG, "_contact in getAllContact is : ${_contacts.value}")
            }
            .addOnFailureListener { exception ->
                // 处理错误
                println("Error getting documents: ${exception.message}")
                _contacts.value = listOf()
            }
//        _reservations.value = db.reservationDao().getReservationByUserId(userid)
//        getCourts(application)
    }
    fun setInvite(uuid:String, friendUID:String){
        val updateU = hashMapOf<String, Any>(
            "status" to "1"
        )
        val updateFriend = hashMapOf<String, Any>(
            "status" to "-1"
        )
        db1.collection("users").document("u$uuid").collection("contact").document("$friendUID")
            .update(updateU).addOnSuccessListener {        }
            .addOnFailureListener { exception ->
                println("Error getting documents: ${exception.message}")
            }
        db1.collection("users").document("u$friendUID").collection("contact").document("$uuid")
            .update(updateFriend).addOnSuccessListener {        }
            .addOnFailureListener { exception ->
                println("Error getting documents: ${exception.message}")
            }
    }
    fun setRej(uuid:String, friendUID:String){
        val updateU = hashMapOf<String, Any>(
            "status" to "3"
        )
        val updateFriend = hashMapOf<String, Any>(
            "status" to "-3"
        )
        db1.collection("users").document("u$uuid").collection("contact").document("$friendUID")
            .update(updateU).addOnSuccessListener {        }
            .addOnFailureListener { exception ->
                println("Error getting documents: ${exception.message}")
            }
        db1.collection("users").document("u$friendUID").collection("contact").document("$uuid")
            .update(updateFriend).addOnSuccessListener {        }
            .addOnFailureListener { exception ->
                println("Error getting documents: ${exception.message}")
            }
    }
    fun acc(uuid:String, friendUID:String){
        val updateU = hashMapOf<String, Any>(
            "status" to "2"
        )
        val updateFriend = hashMapOf<String, Any>(
            "status" to "-2"
        )
        db1.collection("users").document("u$uuid").collection("contact").document("$friendUID")
            .update(updateU).addOnSuccessListener {        }
            .addOnFailureListener { exception ->
                println("Error getting documents: ${exception.message}")
            }
        db1.collection("users").document("u$friendUID").collection("contact").document("$uuid")
            .update(updateFriend).addOnSuccessListener {        }
            .addOnFailureListener { exception ->
                println("Error getting documents: ${exception.message}")
            }
    }

}