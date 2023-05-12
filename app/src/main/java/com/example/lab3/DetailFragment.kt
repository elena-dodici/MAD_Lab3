package com.example.lab3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import android.widget.AdapterView.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentDetailBinding


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : BaseFragment(R.layout.fragment_detail) {

    private lateinit var binding: FragmentDetailBinding
    private val sharedvm : CalendarViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val fragmentBinding = FragmentDetailBinding.inflate(inflater, container, false)
//        binding = fragmentBinding
        binding = FragmentDetailBinding.bind(view, savedInstanceState)

        binding.delBtn.setOnClickListener {
            sharedvm.deleteRes(sharedvm.resIdvm.value!!,this.requireActivity().application)
            findNavController().navigate(R.id.action_detailFragment_to_calendar)
            Toast.makeText(context, "Delete successfully", Toast.LENGTH_LONG).show()
        }
        binding.saveBtn.setOnClickListener {
            sharedvm.addOrUpdateRes(this.requireActivity().application)
            findNavController().navigate(R.id.action_detailFragment_to_calendar)
            Toast.makeText(context, "Update successfully", Toast.LENGTH_LONG).show()
        }
        binding.calBtn.setOnClickListener {

            findNavController().navigate(R.id.action_detailFragment_to_calendar)
            Toast.makeText(context, "Unsaved information", Toast.LENGTH_LONG).show()
        }




        val startTSpinner = binding.startTimeSpinner
        sharedvm.freeStartTimes.observe(viewLifecycleOwner) { newST ->
            val arrayAdapterST =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, newST)
            startTSpinner.adapter = arrayAdapterST

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
                }

            }
        }

//        arrayAdapterST.notifyDataSetChanged()


        val endTSpinner = binding.endTimeSpinner
        sharedvm.freeEndTimes.observe(viewLifecycleOwner) { newET ->
            val arrayAdapterET =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, newET)
            endTSpinner.adapter = arrayAdapterET


            val endTimeDefault =
                arrayAdapterET.getPosition(sharedvm.selectedRes.value!!.endTime)
            endTSpinner.setSelection(endTimeDefault)

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
                }

            }
        }



        val courtNameIdMap =  mutableMapOf<String,Int>()
        for (i in sharedvm.courts.value!!){
            courtNameIdMap[i.name] = i.courtId
        }
        val courtNameSpinner = binding.courtNameSpinner
        val courtNameList = courtNameIdMap.keys.toTypedArray()
        val arrayAdapterCourtName = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,courtNameList )
        courtNameSpinner.adapter = arrayAdapterCourtName
        val courtNameDefault = arrayAdapterCourtName.getPosition(sharedvm.selectedRes.value!!.name)
        courtNameSpinner.setSelection(courtNameDefault)
        courtNameSpinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                sharedvm.selectedRes.value!!.courtId = courtNameIdMap[courtNameList[position]]!!
                sharedvm.getAllFreeSLotByCourtIdAndDate(sharedvm.selectedRes.value!!.courtId, sharedvm.selectedRes.value!!.date,0, application = activity!!.application )

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }


        val courtSportIdMap =  mutableMapOf<String,Int>()
        for (i in sharedvm.courts.value!!){
            courtSportIdMap[i.sport] = i.courtId

        }
        val sportTypeSpinner = binding.sportTypeSpinner
        val arrayAdapterSportType = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,sharedvm.sportList )
        sportTypeSpinner.adapter = arrayAdapterSportType
        val sportTypeDefault = arrayAdapterSportType.getPosition(sharedvm.selectedRes.value!!.sport)
        sportTypeSpinner.setSelection(sportTypeDefault)
        sportTypeSpinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                sharedvm.selectedRes.value!!.sport = sharedvm.sportList[position]
                sharedvm.selectedRes.value!!.courtId = courtSportIdMap[sharedvm.sportList[position]]!!

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.apply {
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

            sharedvm.selectedRes.observe(viewLifecycleOwner) { newRes ->
                binding.editDescription.setText(newRes.date.toString())
            }


    }



}