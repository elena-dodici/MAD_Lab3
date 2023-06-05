package com.example.lab3

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MainViewModel: ViewModel() {

    private var _showNav = MutableLiveData<Boolean>()
    val showNav : LiveData<Boolean> = _showNav
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    var user = 1
    var UID = ""
    fun login(application: Application, email:String,password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    if(auth.currentUser!!.isEmailVerified){
                        UID = auth.currentUser!!.uid
                        println("UID --"+ UID)
                    }else{
                        Toast.makeText(application, "Your email has not been verified.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(application, "Login failed. Check you password and email.", Toast.LENGTH_SHORT).show()
                }
            }

    }

    fun setShowNav(vis:Boolean){
        _showNav.value = vis
    }


}