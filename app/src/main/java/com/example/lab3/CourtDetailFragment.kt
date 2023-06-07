
package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.database.CourtAdapter
import com.example.lab3.database.entity.courtsReviews
import com.example.lab3.databinding.FragmentCourtDetailBinding

class CourtDetailFragment : BaseFragment(R.layout.fragment_court_detail), HasBackButton  {

    companion object {
        fun newInstance() = CourtDetailFragment()
    }

    private lateinit var binding: FragmentCourtDetailBinding
    private val sharedvm: CourtViewModel by activityViewModels()
    private val mainvm: MainViewModel by activityViewModels()

    private lateinit var newRecyclerview: RecyclerView

    override val titleRes: Int = R.string.review_detail
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var newList = mutableListOf<courtsReviews>()
        super.onViewCreated(view, savedInstanceState)
         binding = FragmentCourtDetailBinding.bind(view)
        binding.showCourtName.text = sharedvm.courtName.value
        binding.showAddress.text = sharedvm.courtAddress
        newRecyclerview = binding.recycleView
        newRecyclerview.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        //newList = sharedvm.courtReviews.value!!
        sharedvm.courtReviews.observe(viewLifecycleOwner){
                new ->
            newList = new.toMutableList()
            newRecyclerview.adapter = CourtAdapter(newList)
        }

        binding.apply {
            vm = sharedvm
        }

    }



}