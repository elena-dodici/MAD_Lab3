package com.example.lab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.User

class MainActivity : AppCompatActivity() {
    lateinit var db:AppDatabase
//    lateinit var adapter:MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        db = AppDatabase.getDatabase(application)
//        val u = User(1,"abc","bcd","1234")
//        db.userDao().save(u)
//        val a = db.userDao().getUserById(1)
//        val a = db.userDao().getAll()
//        println(">>>>>>>>>> ${a.value}")

        val b1 = findViewById<Button>(R.id.button1)
        val b2 = findViewById<Button>(R.id.button2)

        b1.setOnClickListener{
            val intent = Intent(this, MyReservationActivity::class.java)
            startActivity(intent)
        }
        b2.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

}