package com.example.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.Reservation
import com.example.lab3.database.entity.User
import com.example.lab3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var db:AppDatabase
//    lateinit var adapter:MainAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_main)
        //initialize db
        db = AppDatabase.getDatabase(application)
//        db.userDao().getAll()
        val u = User("abc","bcd","1234")
        db.userDao().addUser(u)
        //db.userDao().getUserById(1)

        binding.btnWriteData.setOnClickListener {
            print("hahh-------------------")
            writeData()
        }

    }
    //user_id need to retrive
    private fun writeData(){
        val timeSlot = binding.editTimeslot.text.toString()
        val description = binding.editDescription.text.toString()
        val court_id = binding.editCourtId.text.toString()
        if(timeSlot.isNotEmpty() && description.isNotEmpty()){
            val newReservation = Reservation(
                court_id.toInt(),1,0,"25/11/2021","This is test description")
                 db.reservationDao().addReservation(newReservation)
//                 db.ReservationDao().getUserById(1)
            Toast.makeText(this@MainActivity,"Save successfully",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this@MainActivity,"Please enter data",Toast.LENGTH_SHORT).show()
        }
    }

    private fun readData(){

    }

}