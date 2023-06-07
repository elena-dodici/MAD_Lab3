package com.example.lab3.viewmodel

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase
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
                                        }
                                        .addOnFailureListener { exception ->
                                            println("add failed: ${exception.message}")
                                        }
                                } else {
                                    // 邮件发送失败
                                    // 在此处添加相关逻辑
                                }
                            }
                    }
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthUserCollisionException) {
                        // 邮箱已被使用
                        _operationResult.value= false
                        Toast.makeText(application, "This email has been used.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

    }


}