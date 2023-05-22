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



//     initDatabase(db) // add some initial data


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
            CourtReview(1,1,4,"I had a great experience here. The staff was professional and helpful, guiding me through the legal process with patience and clarity. The courtrooms were well-maintained, and the proceedings were conducted fairly and efficiently"),
            CourtReview(1,2,3,"The court demonstrated a high level of professionalism and integrity. The judges were knowledgeable and unbiased, ensuring a fair and just outcome. The support staff was friendly and accommodating, making the entire process less intimidating."),
            CourtReview(1,3,5,"I had a positive experience for here. The judges were highly knowledgeable and demonstrated a deep understanding of the law. The court maintained a respectful and dignified atmosphere, fostering a sense of trust and credibility. Overall, the court provided a fair and transparent judicial process"),
            CourtReview(2,1,3,"The proceedings were conducted in a timely manner, and the judges paid close attention to details. The court facilities were modern and comfortable, creating a conducive environment for justice to be served"),
            CourtReview(2,3,3,"It exceeded my expectations. The judges were not only knowledgeable but also empathetic, considering all aspects of the case before making a decision. The courtrooms were well-equipped, and the entire process was conducted with utmost professionalism"),
            CourtReview(2,4,3,"This is highly organized and transparent. The judges were diligent in explaining legal complexities in a way that was understandable to all parties involved. The court's commitment to fairness and adherence to due process were evident throughout the proceedings"),
            CourtReview(3,1,4," I appreciated the court's commitment to justice."),
//            CourtReview(3,5,1,"faulty net and neglected place overall"),

        )
//        val sportDetails = listOf<SportDetail>(
//            SportDetail(1,"running",3,"achievement") ,
//            SportDetail(1,"basketball",2,"achievement"),
//            SportDetail(1,"swimming",1,"achievement"),
//
//            SportDetail(1,"basketball",4,"achievement"),
//            SportDetail(1,"swimming",1,"achievement"),
//            SportDetail(1,"pingpong",5,"achievement"),
//
//            SportDetail(1,"basketball",1,"achievement"),
//            SportDetail(1,"tennis",5,"achievement"),
//
//        )

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
//        for(s in sportDetails){
//            db.sportDetailDao().addSportDetails(s)
//        }
    }


}