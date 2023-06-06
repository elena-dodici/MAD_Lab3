package com.example.lab3

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
//import com.kizitonwose.calendar.sample.R

interface HasToolbar {
    val toolbar: Toolbar? // Return null to hide the toolbar
}

//interface HasBackButton

abstract class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

//    abstract val titleRes: Int?

    val activityToolbar: Toolbar
        get() = (requireActivity() as MainActivity).binding.activityToolbar

    override fun onStart() {
        super.onStart()
        if (this is HasToolbar) {
            activityToolbar.makeGone()
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
//            (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
//            (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }


    }

    override fun onStop() {
        super.onStop()
        if (this is HasToolbar) {
            activityToolbar.makeVisible()
            (requireActivity() as AppCompatActivity).setSupportActionBar(activityToolbar)
//            (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
//            (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

    }
}
