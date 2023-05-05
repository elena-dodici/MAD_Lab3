package com.example.lab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.User
import com.example.lab3.databinding.ActivityMainBinding
import com.example.lab3.databinding.ActivityMyReservationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    internal lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var db:AppDatabase
//    lateinit var adapter:MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navController = navHostFragment.navController
        val bottonNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        bottonNavigationView.uncheckAllItems()
        setupWithNavController(bottonNavigationView, navController)
        setSupportActionBar(binding.activityToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

//        db = AppDatabase.getDatabase(application)
//        val u = User(1,"abc","bcd","1234")
//        db.userDao().save(u)
//        val a = db.userDao().getUserById(1)
//        val a = db.userDao().getAll()
//        println(">>>>>>>>>> ${a.value}")

//        val b1 = findViewById<Button>(R.id.button1)
//        val b2 = findViewById<Button>(R.id.button2)
//
//        b1.setOnClickListener{
////            val intent = Intent(this, MyReservationActivity::class.java)
////            startActivity(intent)
//            if(savedInstanceState == null){
//                supportFragmentManager.beginTransaction()
//                    .add(R.id.fragmentContainerView,Calendar(),Calendar.javaClass.simpleName)
////                .replace(R.id.fragmentContainerView,Calender(),Calender.javaClass.simpleName )
//                    .addToBackStack(Calendar.javaClass.simpleName)
//                    .commit()
//            }
//        }
//        b2.setOnClickListener{
//            if(savedInstanceState == null){
//                supportFragmentManager.beginTransaction()
//                    .add(R.id.fragmentContainerView,SearchFragment(),SearchFragment.javaClass.simpleName)
////                .replace(R.id.fragmentContainerView,Calender(),Calender.javaClass.simpleName )
//                    .addToBackStack(Calendar.javaClass.simpleName)
//                    .commit()
//            }
//        }
    }
    fun BottomNavigationView.uncheckAllItems() {
        menu.setGroupCheckable(0, true, false)
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
        menu.setGroupCheckable(0, true, true)
    }

}