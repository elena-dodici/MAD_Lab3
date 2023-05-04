package com.example.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.lab3.databinding.ActivityMyReservationBinding

class MyReservationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reservation)

//        val navController = (supportFragmentManager
//            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
//                ).navController
//        navController.navigate(id)

//        if(savedInstanceState == null){
//            supportFragmentManager.beginTransaction()
//            .add(R.id.fragmentContainerView,Calendar(),Calendar.javaClass.simpleName)
//                .commit()
//        }

    }
}