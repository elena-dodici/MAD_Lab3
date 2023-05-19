
package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentCourtDetailBinding


/**
 * A simple [Fragment] subclass.
 * Use the [CourtDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CourtDetailFragment : BaseFragment(R.layout.fragment_court_detail), HasToolbar  {

    private lateinit var binding: FragmentCourtDetailBinding
    private val sharedvm: CourtViewModel by activityViewModels()
    override val toolbar: Toolbar?
        get() = binding.activityToolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_court_detail, container, false)
        return binding.root
        // return inflater.inflate(R.layout.fragment_court_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  binding = CourtDetailFragment.bind(view, savedInstanceState)

        sharedvm.courtName.observe(viewLifecycleOwner){
                newCN ->
            binding.showCourtName.text = newCN.toString()
        }
        binding.apply {
            vm = sharedvm
        }

        if (sharedvm.hasRev.value!!) {
            binding.delBtn.visibility = View.VISIBLE;
        } else binding.delBtn.visibility = View.GONE

        binding.calBtn.setOnClickListener {
            findNavController().navigate(R.id.action_courtDetailFragment_to_courtFragment)
            sharedvm.setShowNav(true)
            Toast.makeText(context, "Your Review is not saved!", Toast.LENGTH_LONG).show()
        }
        binding.delBtn.setOnClickListener {
            //if delete bottom show, it must means has review
            sharedvm.deleteCourtRev(this.requireActivity().application)
            sharedvm.setShowNav(true)
            findNavController().navigate(R.id.action_courtDetailFragment_to_courtFragment)
            Toast.makeText(context, "Delete successfully", Toast.LENGTH_LONG).show()
        }
        binding.saveBtn.setOnClickListener {

            sharedvm.addOrUpdateCourtRev(sharedvm.courtRate.value!!, sharedvm.selectedCourtRev.value!!.review, this.requireActivity().application)
            sharedvm.setShowNav(true)
            findNavController().navigate(R.id.action_courtDetailFragment_to_courtFragment)

            Toast.makeText(context, "Update successfully", Toast.LENGTH_LONG).show()
        }

        val rateSpinner = binding.rateSpinner
        val rateList = listOf<Int>(0,1,2,3,4,5)
        val arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, rateList)
        rateSpinner.adapter = arrayAdapter
        val rateDefault =
            arrayAdapter.getPosition(sharedvm.selectedCourtRev.value!!.rating)
        rateSpinner.setSelection(rateDefault)

        rateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {
                println(rateList[position])
                sharedvm.setRate(rateList[position])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

    }



}