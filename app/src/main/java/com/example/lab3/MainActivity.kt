package com.example.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.User

class MainActivity : AppCompatActivity() {
    lateinit var db:AppDatabase
//    lateinit var adapter:MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = AppDatabase.getDatabase(application)
//        db.userDao().getAll()
        val u = User(1,"abc","bcd","1234")
//        db.userDao().save(u)
        db.userDao().getUserById(1)
    }

}