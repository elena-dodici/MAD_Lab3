package com.example.lab3

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.CourtInfo
import com.example.lab3.database.entity.SportDetail
import com.example.lab3.database.entity.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileViewModel : ViewModel() {

    lateinit var db: AppDatabase
    val db1 = Firebase.firestore
    private var _User = MutableLiveData<User>()
    val User:LiveData<User> = _User
    private var _userSports = MutableLiveData<List<SportDetail>>().also { it.value = listOf() }
    val userSports:LiveData<List<SportDetail>> = _userSports

//    fun getUserById(application: Application, id:Int){
//        db = AppDatabase.getDatabase(application)
//        _User.value = db.userDao().getUserById(id)
//    }
    fun getUserById(application: Application, id:Int){

    db1.collection("users").document("u${id}").get()
        .addOnSuccessListener { result ->
            _User.value = User(result["name"].toString(),result["surname"].toString(),result["tel"].toString())
        }
        .addOnFailureListener { exception ->
            println("Error getting documents: ${exception.message}")
            _User.value = User("","","")
        }

}

    fun updateUser(application: Application, user: User,id: Int){
        val data = hashMapOf(
            "name" to user.name,
            "surname" to user.surname,
            "tel" to user.tel
        )
        val newD=db1.collection("users").document("u${id}")
        newD.update(data as Map<String, Any>)
            .addOnSuccessListener {        }
            .addOnFailureListener { exception ->
                println("Error getting documents: ${exception.message}")
            }
    }

//    fun getUserSportsById(application: Application, id:Int){
//        db = AppDatabase.getDatabase(application)
//        _userSports.value=db.sportDetailDao().getSportDetailsByUserId(id)
//    }
    fun getUserSportsById(application: Application, id:Int){
    db1.collection("users").document("u${id}").collection("sports").get()
        .addOnSuccessListener { result ->
            val tempList:MutableList<SportDetail> = mutableListOf()
            val dataList = result.map { document ->
                document.getString("achievement")?.let {
                    SportDetail(
                        userId = id,
                        sportType = document.id,
                        masteryLevel = document.getLong("level"),
                        achievement = it
                    )
                }

            }

            val sportName  = result.map { document->
                document.id
            }
            dataList.forEach{res-> // 遍历每一个reservation

                if (res != null) {
                    tempList.add(SportDetail(res.userId,res.sportType,res.masteryLevel,res.achievement))
                }
            }
            _userSports.value=tempList

        }
        .addOnFailureListener { exception ->
            println("Error getting documents: ${exception.message}")
            _userSports.value=null
        }
    }

    fun addUserSport(application: Application, sd:SportDetail){
        val id:Int=sd.userId
        val data = hashMapOf(
            "achievement" to sd.achievement,
            "level" to sd.masteryLevel
        )
        val newD=db1.collection("users").document("u${id}").collection("sports").document("${sd.sportType}")
        newD.set(data)
                .addOnSuccessListener {        }
            .addOnFailureListener { exception ->
                println("Error getting documents: ${exception.message}")
            }
    }

    fun updateUserSport(application: Application, sd: SportDetail,id:Int){
        val data = hashMapOf(
            "achievement" to sd.achievement,
            "level" to sd.masteryLevel
        )
        val newD=db1.collection("users").document("u${id}").collection("sports").document("${sd.sportType}")
        newD.update(data as Map<String, Any>)
            .addOnSuccessListener {        }
            .addOnFailureListener { exception ->
                println("Error getting documents: ${exception.message}")
            }
    }

    fun deleteUserSport(application: Application, sd: SportDetail){
        val id:Int=sd.userId
        val newD=db1.collection("users").document("u${id}").collection("sports").document("${sd.sportType}")
        newD.delete()
            .addOnSuccessListener {        }
            .addOnFailureListener { exception ->
                println("Error getting documents: ${exception.message}")
            }
    }
}