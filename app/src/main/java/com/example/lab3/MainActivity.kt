package com.example.lab3

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.*
import com.example.lab3.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

import java.time.LocalDate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Time
import java.util.*


import kotlin.math.log
import java.time.LocalTime

class MainActivity : AppCompatActivity() {
    internal lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    lateinit var db:AppDatabase
    private  val vm: MainViewModel by viewModels()
    val db1 = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        window.statusBarColor = ContextCompat.getColor(this, R.color.deepest_status_color)
        setContentView(binding.root)

//        val vm: MainViewModel by viewModels()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navController = navHostFragment.navController
        val bottonNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavView)
        bottonNavigationView.uncheckAllItems()
        setupWithNavController(bottonNavigationView, navController)
        setSupportActionBar(binding.activityToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.activityToolbar.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId){
                R.id.Logout->{
                    Toast.makeText(this, "logout clicked",Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.message->{
                    Toast.makeText(this, "messgae clicked",Toast.LENGTH_SHORT).show()
                true
                }

                R.id.profile->{
                    Toast.makeText(this, "profile clicked",Toast.LENGTH_SHORT).show()
                    true

            }
                else->{
                false}
            }


        }

        vm.showNav.observe(this){
                show ->
//            println(vm.showNav.value)
            bottonNavigationView.visibility = if (vm.showNav.value == true) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        db = AppDatabase.getDatabase(application)
        //addres()
        //vm.updateCourtTimesDates()

//       initDatabase(db) // add some initial data
      // initFirebase()




    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
//                supportFragmentManager.popBackStack()
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        vm.setShowNav(true)


    }
//fun addres(){
//
//    db1.collection("users").document("uQOjAAjJV1pPkP8uAh1NHgprAEFI3").collection("reservation").document("res5").set(
//        reservation("court3", courtTime(Timestamp(Date(2023 - 1900, 5, 1, 9,0, 0)), Timestamp(Date(2023 - 1900, 5, 1, 10, 0))),
//            "no specific review",-1,"",0, "swimming"),
//    )
//    db1.collection("court").document("court3").collection("courtTime").document("2023-06-01")
//        .update("9",false)
//
//    val newreview = hashMapOf(
//        "user" to "u3",
//        "review" to "the facility stands as a testament to its commitment to providing an exceptional environment for athletes and fitness enthusiasts.",
//        "rating" to "1"
//    )
//    db1.collection("court").document("court3").set(newreview)
//}

    data class reservation(val name:String,val ct: courtTime,val description: String, val rating:Int, val review:String, val status:Int, val sport: String)
    data class courtTime(
        val startTime : Timestamp,
        val endTime:Timestamp
    )
    data class interestSport(val achievement:String, val level:Int)
    data class review(val user:String, val review:String,val rating: Int)
    data class court(val address:String, val avg_rating: Float, val review:List<review>, val sport:String)

    fun initFirebase(){
        val user = hashMapOf(
            "name" to "Ann",
            "surname" to "Johnson",
            "tel" to "2598498759"
        )



        val reservations= listOf<reservation>(
            // u1
            reservation("court1", courtTime(Timestamp(Date(2023 - 1900, 5, 1, 9, 0)), Timestamp(Date(2023 - 1900, 5, 1, 10, 0))),
                            "This is the description for user1's reservation of his running ",4,"The Sport Court's facility is a fun and competitive hub for sports enthusiasts. With its well-maintained courts, modern equipment, and opportunities for organized leagues and tournaments, it offers an exhilarating experience for those seeking both recreation and healthy competition.",0,"running"),
            reservation("court2", courtTime(Timestamp(Date(2023 - 1900, 5, 3, 10, 0)), Timestamp(Date(2023 - 1900, 5, 3, 11, 0))),
                            "No specific requirement",5,"At the Sport Court's facility, the staff exhibits professionalism and friendliness. Their knowledge, approachability, and willingness to assist create a welcoming atmosphere for visitors, making the overall experience enjoyable.",0, "basketball"),
            reservation("court3", courtTime(Timestamp(Date(2023 - 1900, 5, 22, 16, 0)), Timestamp(Date(2023 - 1900, 5, 22, 17, 0))),
                "No specific requirement",-1,"",0, "swimming"),
            //u2
            reservation("court1", courtTime(Timestamp(Date(2023 - 1900, 5, 18, 15, 0)), Timestamp(Date(2023 - 1900, 5, 18, 16, 0))),
                "d1 for u2",-1,"",0, "running"),
            reservation("court1", courtTime(Timestamp(Date(2023 - 1900, 5, 19, 11, 0)), Timestamp(Date(2023 - 1900, 5, 19, 12, 0))),
                "d2 for u2",-1,"",0, "basketball"),
            reservation("court1", courtTime(Timestamp(Date(2023 - 1900, 5, 1, 10, 0)), Timestamp(Date(2023 - 1900, 5, 1, 11, 0))),
                "Please leave some clean towels",4,"The Sport Court's facility is conveniently located and fosters a sense of community. With ample parking, accessible entrances, and organized sports events, it brings people together and encourages a healthy and active lifestyle.",0, "swimming"),

            //u3
            reservation("court5", courtTime(Timestamp(Date(2023 - 1900, 4, 28, 15, 0)), Timestamp(Date(2023 - 1900, 4, 28, 16, 0))),
                "We need some tennis balls",5," The courts are immaculately aintained, with clean surfaces and properly marked boundaries, ensuring a safe and enjoyable playing experience.",0, "tennis"),
            reservation("court4", courtTime(Timestamp(Date(2023 - 1900, 5, 14, 10, 0)), Timestamp(Date(2023 - 1900, 5, 14, 11, 0))),
                "description for user3",-1,"",0, "pingpong"),
            reservation("court3", courtTime(Timestamp(Date(2023 - 1900, 5, 1, 14, 0)), Timestamp(Date(2023 - 1900, 5, 1, 15, 0))),
                "No specific requirement",4," the facility stands as a testament to its commitment to providing an exceptional environment for athletes and fitness enthusiasts.",0, "swimming"),

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
        db1.collection("users").document("u1").collection("reservation").document("res0").set(reservations[0])
        db1.collection("users").document("u1").collection("reservation").document("res1").set(reservations[1])
        db1.collection("users").document("u1").collection("reservation").document("res2").set(reservations[2])

        db1.collection("users").document("u2").collection("reservation").document("res0").set(reservations[3])
        db1.collection("users").document("u2").collection("reservation").document("res1").set(reservations[4])
        db1.collection("users").document("u2").collection("reservation").document("res2").set(reservations[5])

        db1.collection("users").document("u3").collection("reservation").document("res0").set(reservations[6])
        db1.collection("users").document("u3").collection("reservation").document("res1").set(reservations[7])
        db1.collection("users").document("u3").collection("reservation").document("res2").set(reservations[8])

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




        val rev = listOf<review>(

            review("u1",
                "At the Sport Court's facility, the staff exhibits professionalism and friendliness. Their knowledge, approachability, and willingness to assist create a welcoming atmosphere for visitors, making the overall experience enjoyable.",
                5),

        )


        // court
        val courts = listOf<court>(
            court("Via Po 25, Torino, Italy", 4.0f, listOf(
                review(
                    "u1",
                    "The Sport Court's facility is a fun and competitive hub for sports enthusiasts. With its well-maintained courts, modern equipment, and opportunities for organized leagues and tournaments, it offers an exhilarating experience for those seeking both recreation and healthy competition.",
                    4
                ),
                review(
                    "u2",
                    "The Sport Court's facility is conveniently located and fosters a sense of community. With ample parking, accessible entrances, and organized sports events, it brings people together and encourages a healthy and active lifestyle.",
                    4
                ),
            ),
                "running"),
            court("Corso Re Umberto 31, Turin, Italy", 5.0f, listOf(
                review("u1",
                    "At the Sport Court's facility, the staff exhibits professionalism and friendliness. Their knowledge, approachability, and willingness to assist create a welcoming atmosphere for visitors, making the overall experience enjoyable.",
                    5)),
                "basketball"),
            court("Via Roma 123, Turin, Italy", 4.0f, listOf(
                review("u3",
                    "the facility stands as a testament to its commitment to providing an exceptional environment for athletes and fitness enthusiasts.",
                    4)),
                "swimming"),
            court("Corso Vittorio Emanuele II 45, Turin, Italy", 0.0f, listOf(),
                "pingpong"),
            court("Via Giuseppe Garibaldi 5, Turin, Italy", 5.0f, listOf(
                review("u1",
                    "The Sport Court's facility is a fun and competitive hub for sports enthusiasts. With its well-maintained courts, modern equipment, and opportunities for organized leagues and tournaments, it offers an exhilarating experience for those seeking both recreation and healthy competition.",
                    5)
            ),
                "tennis"),



        )

//        db1.collection("court").document("court1").set(courts[0])
//        db1.collection("court").document("court2").set(courts[1])
        for ((i,c) in courts.withIndex()){
            db1.collection("court").document("court${i+1}").set(c)
        }
        // court time
        val dates = listOf<String>(
            "2023-05-27", "2023-05-28", "2023-05-29", "2023-05-30", "2023-05-31",
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


        db1.collection("court").document("court1").collection("courtTime").document("2023-06-01")
            .update("9",false)


        db1.collection("court").document("court1").collection("courtTime").document("2023-06-01")
            .update("10",false)


        db1.collection("court").document("court1").collection("courtTime").document("2023-06-18")
            .update("15",false)


        db1.collection("court").document("court1").collection("courtTime").document("2023-06-19")
            .update("11",false)


        db1.collection("court").document("court2").collection("courtTime").document("2023-06-03")
            .update("10",false)


        db1.collection("court").document("court3").collection("courtTime").document("2023-06-22")
            .update("16",false)

        db1.collection("court").document("court3").collection("courtTime").document("2023-06-01")
            .update("14",false)

        db1.collection("court").document("court4").collection("courtTime").document("2023-06-14")
            .update("10",false)


        db1.collection("court").document("court5").collection("courtTime").document("2023-05-28")
            .update("15",false)




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