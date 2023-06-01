package com.example.lab3

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import com.example.lab3.database.AppDatabase
import com.example.lab3.database.entity.CourtInfo
import com.example.lab3.database.entity.SportDetail
import com.example.lab3.database.entity.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileViewModel : ViewModel() {

    lateinit var db: AppDatabase
    val db1 = Firebase.firestore
    val storage = Firebase.storage
    private var _User = MutableLiveData<UserProfile>()
    val User:LiveData<UserProfile> = _User
    private var _userSports = MutableLiveData<List<SportDetail>>().also { it.value = listOf() }
    val userSports:LiveData<List<SportDetail>> = _userSports

//    fun getUserById(application: Application, id:Int){
//        db = AppDatabase.getDatabase(application)
//        _User.value = db.userDao().getUserById(id)
//    }
public data class UserProfile(
    var name:String,
    var surname:String,
    var tel:String?,
    var photo:String?
)
    fun getUserById(application: Application, id:Int){

    db1.collection("users").document("u${id}").get()
        .addOnSuccessListener { result ->
            _User.value = UserProfile(result["name"].toString(),result["surname"].toString(),result["tel"].toString(),result["photo"].toString())
        }
        .addOnFailureListener { exception ->
            println("Error getting documents: ${exception.message}")
            _User.value = UserProfile("","","","")
        }

}
    fun uploadPhoto(application: Application, photo: Uri,id:Int){
        val storageRef = storage.reference
        val storagePath = "user${id}/images/" // 存储路径，可以根据自己的需要进行调整
        val fileName = "user${id}.jpg" // 图片文件名，可以根据自己的需要进行调整
        val fileRef = storageRef.child("$storagePath$fileName")
        val uploadTask = fileRef.putFile(photo)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // 图片文件上传成功
            println("upload success")
        }.addOnFailureListener { exception ->
            // 处理上传失败的情况
        }.addOnProgressListener { taskSnapshot ->
            // 监听上传进度
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toInt()
            // 在此处可以更新上传进度条或显示上传进度信息
        }
    }

    private var _Uri = MutableLiveData<String>()
    val photoUri:LiveData<String> = _Uri
    fun readPhoto(application: Application,path:String){
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child(path)
        imageRef.downloadUrl.addOnSuccessListener { uri ->

            _Uri.value = uri.toString()
            println(_Uri.value)
        }.addOnFailureListener { exception ->
            // 处理下载失败的情况
            println("Error downloading image: ${exception.message}")
        }

    }

    fun updateUser(application: Application, user: UserProfile,id: Int){
        val data = hashMapOf(
            "name" to user.name,
            "surname" to user.surname,
            "tel" to user.tel,
            "photo" to user.photo
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