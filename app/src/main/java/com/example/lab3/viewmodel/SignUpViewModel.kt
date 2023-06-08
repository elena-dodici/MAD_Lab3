package com.example.lab3.viewmodel

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.Contact
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.MyReservation
import com.example.lab3.database.entity.SportDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SignUpViewModel : ViewModel() {

    lateinit var db: AppDatabase
    val db1 = Firebase.firestore
    private var auth: FirebaseAuth=FirebaseAuth.getInstance()
    private lateinit var uid :String
    private val _operationResult = MutableLiveData<Boolean>()
    val operationResult: LiveData<Boolean> get() = _operationResult

    private var _userIdList = MutableLiveData<List<Contact>>()
    val userIdList:LiveData<List<Contact>> = _userIdList

    fun signUp(application: Application, email:String,password:String, name:String,surname:String,tel:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    uid= auth.currentUser!!.uid
                    if (user != null) {
                        user.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val u = hashMapOf(
                                        "name" to name,
                                        "photo" to "",
                                        "surname" to surname,
                                        "tel" to tel)
                                    db1.collection("users").document("u${uid}").set(u)
                                        .addOnSuccessListener {
                                            _operationResult.value = true
                                            // maintain contacts
                                            db1.collection("users").get()
                                                .addOnSuccessListener { result ->
                                                    val dataList = result.map { document ->
                                                        val mapId = mapOf(
                                                            "userid" to document.id,
                                                        )
                                                        mapId + document.data
                                                    }
//                                                    val allContact = mutableListOf<Contact>()
                                                    dataList.forEach { contact -> // 遍历每一个contact
//                                                        allContact.add(Contact(contact["userid"].toString(), contact["name"].toString(), 0))
                                                        if (contact["userid"].toString() != "u${uid}") {
                                                            db1.collection("users")
                                                                .document("u${uid}")
                                                                .collection("contact")
                                                                .document(contact["userid"].toString())
                                                                .set(
                                                                    hashMapOf(
                                                                        "name" to contact["name"].toString(),
                                                                        "status" to 0
                                                                    )
                                                                )
                                                            db1.collection("users").document(contact["userid"].toString()).collection("contact").document("u${uid}")
                                                                .set(
                                                                    hashMapOf(
                                                                        "name" to name,
                                                                        "status" to 0
                                                                    )
                                                                )
                                                        }
                                                    }

                                                }
                                        }
                                        .addOnFailureListener { exception ->
                                            println("add failed: ${exception.message}")
                                        }
                                } else {
                                    // send email error
                                }
                            }
                    }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
                        // email has occupied
                        _operationResult.value= false
                        Toast.makeText(application, "This email has been used.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }


}