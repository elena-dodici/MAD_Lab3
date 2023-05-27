package com.example.lab3

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.example.lab3.databinding.FragmentMainBinding


class MainFragment : BaseFragment(R.layout.fragment_main), HasToolbar {
    companion object {
        fun newInstance() = MainFragment()
    }
    private lateinit var binding: FragmentMainBinding
    override val toolbar: Toolbar?
        get() = binding.activityToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)


    }
}