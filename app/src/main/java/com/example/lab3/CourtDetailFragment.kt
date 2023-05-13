package com.example.lab3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.lab3.databinding.FragmentDetailBinding



/**
 * A simple [Fragment] subclass.
 * Use the [CourtDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CourtDetailFragment : BaseFragment(R.layout.fragment_court_detail) {

    private lateinit var binding: FragmentDetailBinding
    private val sharedvm : CourtViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_court_detail, container, false)
    }


}