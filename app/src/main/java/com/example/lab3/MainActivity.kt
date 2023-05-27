package com.example.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.Court
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.database.entity.Reservation
import com.example.lab3.database.entity.User
import com.example.lab3.databinding.ActivityMainBinding
import java.sql.Time
import java.time.LocalDate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


import kotlin.math.log

class MainActivity : AppCompatActivity() {
    internal lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var db:AppDatabase
    val db1 = Firebase.firestore

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

        db = AppDatabase.getDatabase(application)



//       initDatabase(db) // add some initial data
        initFirebase()





    }

    data class reservation(val name:String,val ct: courtTime,val description: String, val rating:Int, val review:String, val status:Int)
    data class courtTime(
        val startTime : Timestamp,
        val endTime:Timestamp
    )
    data class interestSport(val achievement:String, val level:Int)
    data class review(val user:String, val review:String)
    data class court(val address:String, val name: String, val avg_rating: Float, val review:List<review>, val sport:String)

    fun initFirebase(){
        val user = hashMapOf(
            "name" to "Ann",
            "surname" to "Johnson",
            "tel" to "2598498759"
        )

        val res1 = hashMapOf(
            "courtTime" to  "9:00 - 10:00",
            "achievement" to "3rd price",
        )

        val reservations= listOf<reservation>(
            reservation("bbcourt1", courtTime(Timestamp(Date(2023 - 1900, 0, 5, 9, 0)), Timestamp(Date(2023 - 1900, 0, 5, 10, 0))),
                            "d1 for u",2,"review1",0),
            reservation("bbcourt2", courtTime(Timestamp(Date(2023 - 1900, 0, 6, 10, 0)), Timestamp(Date(2023 - 1900, 0, 6, 11, 0))),
                            "d2 for u",3,"review2",0),
            reservation("bbcourt3", courtTime(Timestamp(Date(2023 - 1900, 0, 7, 10, 0)), Timestamp(Date(2023 - 1900, 0, 7, 11, 0))),
                           "d3 for u",4,"review3",0),
        )
//        for (i in 1..3){
//            for ((id,r) in reservations.withIndex()){
//                db1.collection("users").document("u${i}").collection("reservation").document("res${id}").set(r)
//                    .addOnSuccessListener { documentReference->
//                        Log.d("firestore","DocumentSnapshot added")
//                    }
//                    .addOnFailureListener{e->
//                        Log.w("fireshore","Error")
//                    }
//            }
//        }
        val s = listOf("running", "basketball", "swimming","pingpong","tennis")


        val interestSports = listOf<interestSport>(
            interestSport("asd",1),
            interestSport("3rd price",1),
            interestSport("1st price",5)
        )
        // interesting sports for each user
//        db1.collection("users").document("u1")
//            .collection("sports").document(s[1]).set(interestSports[0])
//
//        db1.collection("users").document("u2")
//            .collection("sports").document(s[3]).set(interestSports[1])
//
//        db1.collection("users").document("u3")
//            .collection("sports").document(s[0]).set(interestSports[2])

        // court
        val courts = listOf<court>(
            court("via po 1", "rcourt1", 2.0f, listOf( review("u1","rev1foru1")), s[0]),
            court("via po 2", "bbcourt2", 3.5f,listOf(review("u2","rev2foru1")), s[1]),
            court("via po 3", "swcourt3", 2.0f,listOf(review("u1","rev3foru1")), s[2]),
            court("via po 4", "ppcourt4", 2.0f,listOf(review("u1","rev4foru1")), s[3]),
            court("via po 5", "tcourt5", 2.0f,listOf(review("u1","rev5foru1")), s[4]),

        )
        for ((i,c) in courts.withIndex()){
            db1.collection("court").document("court${i+1}").set(c)
        }
        // court time
        val dates = listOf<String>("2023-05-27", "2023-05-28", "2023-05-29", "2023-05-30", "2023-05-31",
            "2023-06-01", "2023-06-02", "2023-06-03", "2023-06-04", "2023-06-05",
            "2023-06-06", "2023-06-07", "2023-06-08", "2023-06-09", "2023-06-10",
            "2023-06-11", "2023-06-12", "2023-06-13", "2023-06-14", "2023-06-15",
            "2023-06-16", "2023-06-17", "2023-06-18", "2023-06-19", "2023-06-20",
            "2023-06-21", "2023-06-22", "2023-06-23", "2023-06-24", "2023-06-25",
            "2023-06-26", "2023-06-27", "2023-06-28", "2023-06-29", "2023-06-30",
        )

        val timeslot = hashMapOf<String, Boolean>()

        for (i in 9..19) {
            timeslot[i.toString()] = true // true 表示free
        }
        for (date in dates){
            for (i in 1..5)
                db1.collection("court").document("court${i}").collection("courtTime").document(date).set(timeslot)
        }
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
    }


}