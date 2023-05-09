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
import com.example.lab3.database.entity.Court
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


        var freeSlotEndList = ArrayList<Time>()
        for (i in sharedvm.freeEndTimes.value!!){
            freeSlotEndList.add(i)
        }

        val startTSpinner = binding!!.startTimeSpinner
        sharedvm.freeStartTimes.observe(viewLifecycleOwner) { newST ->
            val arrayAdapterST =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, newST)
            startTSpinner.adapter = arrayAdapterST

            println(sharedvm.selectedRes.value!!.startTime)
            val startTimeDefault =
                arrayAdapterST.getPosition(sharedvm.selectedRes.value!!.startTime)
            startTSpinner.setSelection(startTimeDefault)

            startTSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    sharedvm.selectedRes.value!!.startTime = newST[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }

//        arrayAdapterST.notifyDataSetChanged()





        val endTSpinner = binding!!.endTimeSpinner
        sharedvm.freeStartTimes.observe(viewLifecycleOwner) { newET ->
            val arrayAdapterET =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, newET)
            endTSpinner.adapter = arrayAdapterET


            val endTimeDefault =
                arrayAdapterET.getPosition(sharedvm.selectedRes.value!!.endTime)
            startTSpinner.setSelection(endTimeDefault)

            endTSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    sharedvm.selectedRes.value!!.endTime = newET[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
        


        var courtNameIdMap =  mutableMapOf<String,Int>()
        for (i in sharedvm.courts.value!!){
            courtNameIdMap[i.name] = i.courtId
        }
        val courtNameSpinner = binding!!.courtNameSpinner
        val courtNameList = courtNameIdMap.keys.toTypedArray()
        val arrayAdapterCourtName = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,courtNameList )
        courtNameSpinner.adapter = arrayAdapterCourtName
        val courtNameDefault = arrayAdapterCourtName.getPosition(sharedvm.selectedRes.value!!.name)
        courtNameSpinner.setSelection(courtNameDefault)
        courtNameSpinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                println(courtNameIdMap[courtNameList[position]])
                sharedvm.selectedRes.value!!.courtId = courtNameIdMap[courtNameList[position]]!!
                sharedvm.getAllFreeSLotByCourtIdAndDate(sharedvm.selectedRes.value!!.courtId, sharedvm.selectedRes.value!!.date,0, application = activity!!.application )

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        var courtSportIdMap =  mutableMapOf<String,Int>()
        for (i in sharedvm.courts.value!!){
            courtSportIdMap[i.sport] = i.courtId

        }
        val sportTypeSpinner = binding!!.sportTypeSpinner
        val arrayAdapterSportType = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,sharedvm.sportList )
        sportTypeSpinner.adapter = arrayAdapterSportType
        val sportTypeDefault = arrayAdapterSportType.getPosition(sharedvm.selectedRes.value!!.sport)
        sportTypeSpinner.setSelection(sportTypeDefault)
        sportTypeSpinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                sharedvm.selectedRes.value!!.sport = sharedvm.sportList[position]
                sharedvm.selectedRes.value!!.courtId = courtSportIdMap[sharedvm.sportList[position]]!!
                sharedvm.getAllFreeSLotByCourtIdAndDate(sharedvm.selectedRes.value!!.courtId, sharedvm.selectedRes.value!!.date,0, application = activity!!.application)
                for(i in sharedvm.freeStartTimes.value!! ){
                    println(i)
                }




            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        return fragmentBinding.root

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

            sharedvm.selectedRes.observe(viewLifecycleOwner, { newRes ->
                binding!!.editDescription.setText(newRes.date.toString())
            })



    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}