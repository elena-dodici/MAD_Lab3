package com.example.lab3.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.SportDetail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class SignUpViewModel : ViewModel() {

    lateinit var db: AppDatabase
    val db1 = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    val storage = Firebase.storage


    fun signUp(application: Application, email:String,password:String){
        // Initialize Firebase Auth
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    println("success")
                    val user = auth.currentUser
                    if (user != null) {
                        user.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {


                                } else {
                                    // 邮件发送失败
                                    // 在此处添加相关逻辑
                                }
                            }
                    }
                } else {
                    println("fail")
                }
            }


    }
    fun newUser(application: Application, name:String,surname:String,tel:String){
        val userid = FirebaseAuth.getInstance().currentUser?.uid
        println(userid)
        val u = hashMapOf(
            "name" to name,
            "photo" to "",
            "surname" to surname,
            "tel" to tel)
        val emptyOrders = hashMapOf<String, Any?>()
        println("name="+name)
        db1.collection("users").document("u4").set(u)
            .addOnSuccessListener {
                println("addsuccess")
            }
            .addOnFailureListener { exception ->
                println("add failed: ${exception.message}")
            }

                db1.collection("users").document("u5").collection("sports")
                db1.collection("users").document("u6").collection("reservation")



    }


}