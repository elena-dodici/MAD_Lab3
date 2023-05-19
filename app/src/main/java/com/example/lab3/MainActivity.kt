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
    private lateinit var vm: CourtViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this)[CourtViewModel::class.java]
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



     //initDatabase(db) // add some initial data


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
            User("aaa","aaa","222222"),
            User("bbb","bbb","222222"),
            User("ccc","ccc","222222"),
        )
        val courts = listOf<Court>(
            Court("Court_1BB","qaz","basketball"),
            Court("Court_2RUN","wsx","running"),
            Court("Court_3SWIM","edc","swimming"),
            Court("Court_4PP","rfv","pingpong"),
            Court("Court_5T","rrv","tennis"),
        )
        @Suppress("DEPRECATION")
        val ct = listOf<CourtTime>(
            CourtTime(1, Time(9,0,0),Time(10,0,0)),
            CourtTime(1, Time(10,0,0),Time(11,0,0)),
            CourtTime(1, Time(11,0,0),Time(12,0,0)),
            CourtTime(1, Time(12,0,0),Time(13,0,0)),
            CourtTime(1, Time(13,0,0),Time(14,0,0)),
            CourtTime(1, Time(14,0,0),Time(15,0,0)),
            CourtTime(1, Time(15,0,0),Time(16,0,0)),
            CourtTime(1, Time(16,0,0),Time(17,0,0)),
            CourtTime(1, Time(17,0,0),Time(18,0,0)),
            CourtTime(1, Time(18,0,0),Time(19,0,0)),

            CourtTime(2, Time(9,0,0),Time(10,0,0)),
            CourtTime(2, Time(10,0,0),Time(11,0,0)),
            CourtTime(2, Time(11,0,0),Time(12,0,0)),
            CourtTime(2, Time(12,0,0),Time(13,0,0)),
            CourtTime(2, Time(13,0,0),Time(14,0,0)),
            CourtTime(2, Time(14,0,0),Time(15,0,0)),
            CourtTime(2, Time(15,0,0),Time(16,0,0)),
            CourtTime(2, Time(16,0,0),Time(17,0,0)),
            CourtTime(2, Time(17,0,0),Time(18,0,0)),
            CourtTime(2, Time(18,0,0),Time(19,0,0)),

            CourtTime(3, Time(9,0,0),Time(10,0,0)),
            CourtTime(3, Time(10,0,0),Time(11,0,0)),
            CourtTime(3, Time(11,0,0),Time(12,0,0)),
            CourtTime(3, Time(12,0,0),Time(13,0,0)),
            CourtTime(3, Time(13,0,0),Time(14,0,0)),
            CourtTime(3, Time(14,0,0),Time(15,0,0)),
            CourtTime(3, Time(15,0,0),Time(16,0,0)),
            CourtTime(3, Time(16,0,0),Time(17,0,0)),
            CourtTime(3, Time(17,0,0),Time(18,0,0)),
            CourtTime(3, Time(18,0,0),Time(19,0,0)),

            CourtTime(4, Time(9,0,0),Time(10,0,0)),
            CourtTime(4, Time(10,0,0),Time(11,0,0)),
            CourtTime(4, Time(11,0,0),Time(12,0,0)),
            CourtTime(4, Time(12,0,0),Time(13,0,0)),
            CourtTime(4, Time(13,0,0),Time(14,0,0)),
            CourtTime(4, Time(14,0,0),Time(15,0,0)),
            CourtTime(4, Time(15,0,0),Time(16,0,0)),
            CourtTime(4, Time(16,0,0),Time(17,0,0)),
            CourtTime(4, Time(17,0,0),Time(18,0,0)),
            CourtTime(4, Time(18,0,0),Time(19,0,0)),

            CourtTime(5, Time(9,0,0),Time(10,0,0)),
            CourtTime(5, Time(10,0,0),Time(11,0,0)),
            CourtTime(5, Time(11,0,0),Time(12,0,0)),
            CourtTime(5, Time(12,0,0),Time(13,0,0)),
            CourtTime(5, Time(13,0,0),Time(14,0,0)),
            CourtTime(5, Time(14,0,0),Time(15,0,0)),
            CourtTime(5, Time(15,0,0),Time(16,0,0)),
            CourtTime(5, Time(16,0,0),Time(17,0,0)),
            CourtTime(5, Time(17,0,0),Time(18,0,0)),
            CourtTime(5, Time(18,0,0),Time(19,0,0)),
        )

        val reservations = listOf<Reservation>(
            Reservation(1, 1, 0, LocalDate.of(2023,5,1),"res1"),
            Reservation(2, 1, 0, LocalDate.of(2023,5,4),"res2"),
            Reservation(3, 1, 0, LocalDate.of(2023,5,1),"res3"),
            Reservation(11, 1, 0, LocalDate.of(2023,5,1),"res4"),
            Reservation(12, 1, 0, LocalDate.of(2023,5,6),"res5"),
            Reservation(13, 1, 0, LocalDate.of(2023,5,1),"res6"),
            Reservation(21, 1, 0, LocalDate.of(2023,4,23),"res7"),
            Reservation(22, 1, 0, LocalDate.of(2023,5,1),"res8"),
            Reservation(23, 1, 0, LocalDate.of(2023,5,4),"res9"),

            Reservation(11, 1, 0, LocalDate.of(2023,5,3),"res9"),
            Reservation(12, 1, 0, LocalDate.of(2023,5,3),"res9"),
            Reservation(13, 1, 0, LocalDate.of(2023,5,3),"res9"),
            Reservation(14, 1, 0, LocalDate.of(2023,5,3),"res9"),
            Reservation(15, 1, 0, LocalDate.of(2023,5,3),"res9"),
            Reservation(16, 1, 0, LocalDate.of(2023,5,3),"res9"),
            Reservation(17, 1, 0, LocalDate.of(2023,5,3),"res9"),
            Reservation(18, 1, 0, LocalDate.of(2023,5,3),"res9"),
            Reservation(19, 1, 0, LocalDate.of(2023,5,3),"res9"),
            Reservation(20, 1, 0, LocalDate.of(2023,5,3),"res9"),


            )
        val courtReviews = listOf<CourtReview>(
            CourtReview(1,1,4,"good!"),
            CourtReview(1,2,3,"g!"),
            CourtReview(1,3,5,"almost perfect"),
            CourtReview(2,1,3,"good!"),
            CourtReview(2,3,3,"good but i've seen better"),
            CourtReview(2,4,3,"best tables i've seen so far"),
            CourtReview(3,1,4,"no problems at all"),
//            CourtReview(3,5,1,"faulty net and neglected place overall"),

        )
        val sportDetails = listOf<SportDetail>(
            SportDetail(1,"running",3,"achievement") ,
            SportDetail(1,"basketball",2,"achievement"),
            SportDetail(1,"swimming",1,"achievement"),

            SportDetail(1,"basketball",4,"achievement"),
            SportDetail(1,"swimming",1,"achievement"),
            SportDetail(1,"pingpong",5,"achievement"),

            SportDetail(1,"basketball",1,"achievement"),
            SportDetail(1,"tennis",5,"achievement"),

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
        for (r in reservations){
            db.reservationDao().addReservation(r)
        }
        for(cr in courtReviews){
            db.courtReviewDao().addCourtReview(cr)
        }
        for(s in sportDetails){
            db.sportDetailDao().addSportDetails(s)
        }
    }


}