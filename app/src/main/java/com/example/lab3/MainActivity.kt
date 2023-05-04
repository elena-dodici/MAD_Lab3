package com.example.lab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.User
import com.example.lab3.databinding.ActivityMainBinding
import com.example.lab3.databinding.ActivityMyReservationBinding

class MainActivity : AppCompatActivity() {
    internal lateinit var binding: ActivityMainBinding

    lateinit var db:AppDatabase
//    lateinit var adapter:MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.activityToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        db = AppDatabase.getDatabase(application)
//        val u = User(1,"abc","bcd","1234")
//        db.userDao().save(u)
//        val a = db.userDao().getUserById(1)
//        val a = db.userDao().getAll()
//        println(">>>>>>>>>> ${a.value}")

        val b1 = findViewById<Button>(R.id.button1)
        val b2 = findViewById<Button>(R.id.button2)

        b1.setOnClickListener{
//            val intent = Intent(this, MyReservationActivity::class.java)
//            startActivity(intent)
            if(savedInstanceState == null){
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainerView,Calendar(),Calendar.javaClass.simpleName)
//                .replace(R.id.fragmentContainerView,Calender(),Calender.javaClass.simpleName )
                    .addToBackStack(Calendar.javaClass.simpleName)
                    .commit()
            }
        }
        b2.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

}