package com.example.lab3

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.*
import androidx.databinding.InverseMethod
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.lab3.database.entity.CourtTime
import com.example.lab3.databinding.FragmentDetailBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.sql.Time
import java.time.LocalDate

/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : BaseFragment(R.layout.fragment_calendar_view) {
    // TODO: Rename and change types of parameters

    private var binding: FragmentDetailBinding? = null
    private val sharedvm : CalendarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentDetailBinding.inflate(inflater, container, false)
        binding = fragmentBinding
//
//        sharedvm.selectedRes.observe(viewLifecycleOwner, { newRes ->
////            binding!!.editStartTime.setText(newRes.startTime.toString())
////            binding!!.editEndTime.setText(newRes.endTime.toString())
//            binding!!.showDate.setText(newRes.date.toString())
//            binding!!.editDate.setText(newRes.date.toString())
//        })

        binding!!.delBtn.setOnClickListener {
            sharedvm.deleteRes(sharedvm.resIdvm.value!!,this.requireActivity().application)
            findNavController().navigate(R.id.action_detailFragment_to_calendar)
            Toast.makeText(context, "Delete successfully", Toast.LENGTH_LONG).show()
        }
        binding!!.saveBtn.setOnClickListener {
            sharedvm.addOrUpdateRes(this.requireActivity().application)
            findNavController().navigate(R.id.action_detailFragment_to_calendar)
            Toast.makeText(context, "Update successfully", Toast.LENGTH_LONG).show()
        }

        binding!!.calBtn.setOnClickListener {

            findNavController().navigate(R.id.action_detailFragment_to_calendar)
            Toast.makeText(context, "Unsaved information", Toast.LENGTH_LONG).show()
        }

        var freeSlotStartList = ArrayList<Time>()
        for (i in sharedvm.freeCourtTimes.value!!){
            freeSlotStartList.add(i.startTime)
        }

        var freeSlotEndList = ArrayList<Time>()
        for (i in sharedvm.freeCourtTimes.value!!){
            freeSlotEndList.add(i.endTime)
        }
        val startSpinner = binding!!.startTimeSpinner
        val arrayAdapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, freeSlotStartList)
        startSpinner.adapter = arrayAdapter1
        startSpinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
//                binding!!.editStartTime.setText(freeSlotList[position].toString())
                sharedvm.selectedRes.value!!.startTime = freeSlotStartList[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        val endSpinner = binding!!.endTimeSpinner
        val arrayAdapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, freeSlotEndList)
        endSpinner.adapter = arrayAdapter2
        endSpinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
//                binding!!.editStartTime.setText(freeSlotList[position].toString())
                sharedvm.selectedRes.value!!.endTime = freeSlotEndList[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }



        return fragmentBinding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        binding?.apply {
            fun max(){
                vm = sharedvm
            }
            max()
            btnTimePicker.setOnClickListener{
                val datePickerFragment = DatePickerFragment(::max)
                val supportFragmentManager = requireActivity().supportFragmentManager
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
        }


    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}