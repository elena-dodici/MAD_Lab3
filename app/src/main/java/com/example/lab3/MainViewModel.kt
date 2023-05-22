package com.example.lab3

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private var _showNav = MutableLiveData<Boolean>()
    val showNav : LiveData<Boolean> = _showNav

    var user:String="1"

    fun setShowNav(vis:Boolean){
        _showNav.value = vis
    }
}