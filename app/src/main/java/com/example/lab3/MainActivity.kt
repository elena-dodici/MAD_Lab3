package com.example.lab3

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.*
import com.example.lab3.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

class MainActivity : AppCompatActivity() {
    internal lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var db:AppDatabase
    private  val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val vm: MainViewModel by viewModels()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navController = navHostFragment.navController
        val bottonNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        bottonNavigationView.uncheckAllItems()
        setupWithNavController(bottonNavigationView, navController)
        setSupportActionBar(binding.activityToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)



        vm.showNav.observe(this){
                show ->
            println(vm.showNav.value)
            bottonNavigationView.visibility = if (vm.showNav.value == true) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        db = AppDatabase.getDatabase(application)



    initDatabase(db) // pre_saved the data in our DB


    }

    override fun onBackPressed() {
        super.onBackPressed()
        vm.setShowNav(true)


    }
    fun BottomNavigationView.uncheckAllItems() {
        menu.setGroupCheckable(0, true, false)
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
        menu.setGroupCheckable(0, true, true)
    }
    private fun initDatabase(db:AppDatabase){
//        var db = AppDatabase.getDatabase(application)
//        build data
        val users = listOf<User>(
            User("John","Wong","+393259874588"),
            User("Mark","Joshnon","+393289548752"),
            User("Ann","Kumar","+392157895468"),
        )
        val courts = listOf<Court>(
            Court("Court_1BacketBall Player","Via Roma 23, Turin,Italy","basketball"),
            Court("Court_2RUNNER","Via Po 25, Torino, Italy","running"),
            Court("Court_3POOLING","Via Garibaldi 122,Turin, Italy","swimming"),
            Court("Court_4TABLEFUN","Corso Re Umberto 34, Turin, Italy","pingpong"),
            Court("Court_5TENNIS","Corso Vittorio Emanuele II, Turin, Italy","tennis"),
        )
        @Suppress("DEPRECATION")
        val ct = listOf<CourtTime>(

            CourtTime(1, Time(10,0,0),Time(11,0,0)),
            CourtTime(1, Time(11,0,0),Time(12,0,0)),

            CourtTime(1, Time(14,0,0),Time(15,0,0)),
            CourtTime(1, Time(15,0,0),Time(16,0,0)),
            CourtTime(1, Time(16,0,0),Time(17,0,0)),



            CourtTime(2, Time(13,0,0),Time(14,0,0)),
            CourtTime(2, Time(14,0,0),Time(15,0,0)),
            CourtTime(2, Time(15,0,0),Time(16,0,0)),
            CourtTime(2, Time(16,0,0),Time(17,0,0)),
            CourtTime(2, Time(17,0,0),Time(18,0,0)),
            CourtTime(2, Time(18,0,0),Time(19,30,0)),

            CourtTime(3, Time(9,0,0),Time(10,0,0)),
            CourtTime(3, Time(10,0,0),Time(11,0,0)),
            CourtTime(3, Time(11,0,0),Time(12,0,0)),
            CourtTime(3, Time(12,0,0),Time(13,0,0)),


            CourtTime(4, Time(9,0,0),Time(10,0,0)),
            CourtTime(4, Time(10,0,0),Time(11,0,0)),
            CourtTime(4, Time(11,0,0),Time(12,0,0)),





            CourtTime(5, Time(15,0,0),Time(16,0,0)),
            CourtTime(5, Time(16,0,0),Time(17,0,0)),
            CourtTime(5, Time(17,0,0),Time(18,0,0)),
            CourtTime(5, Time(18,0,0),Time(19,0,0)),
            CourtTime(5, Time(19,0,0),Time(20,0,0)),
        )

//        insert data
        for ( u in users){
            db.userDao().addUser(u)
        }
        for ( c in courts){
            db.courtDao().addCourt(c)
        }
        for (c in ct){
            db.courtTimeDao().addCourtTime(c)
        }

    }


}