package com.example.lab3

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private var _showNav = MutableLiveData<Boolean>()
    val showNav : LiveData<Boolean> = _showNav

    var _user = MutableLiveData<Int>()
    val user: LiveData<Int> = _user

    fun setShowNav(vis:Boolean){
        _showNav.value = vis
    }

    fun setUser(user:Int){
        _user.value = user
    }
}