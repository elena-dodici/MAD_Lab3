package com.example.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment

class MyReservationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reservation)

//        val navController = (supportFragmentManager
//            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
//                ).navController
//        navController.navigate(id)
        println("MyReservationActivity")
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView,Calendar(),Calendar.javaClass.simpleName)
//                .replace(R.id.fragmentContainerView,Calender(),Calender.javaClass.simpleName )
                .commit()
        }

    }
}