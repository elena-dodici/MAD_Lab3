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
            if(sharedvm.selEndTime.value!! < sharedvm.selStartTime.value) {
                Toast.makeText(context, "End Time must be later than Start Time", Toast.LENGTH_LONG).show()
            }
            else{
                sharedvm.addOrUpdateRes(this.requireActivity().application)
                findNavController().navigate(R.id.action_detailFragment_to_calendar)
                Toast.makeText(context, "Update successfully", Toast.LENGTH_LONG).show()
            }

        }
        binding.calBtn.setOnClickListener {

            findNavController().navigate(R.id.action_detailFragment_to_calendar)
            Toast.makeText(context, "Unsaved information", Toast.LENGTH_LONG).show()
        }


        sharedvm.selDate.observe(viewLifecycleOwner){
            newSelDate ->
            binding.showDate.setText(newSelDate.toString())
        }

        val startTSpinner = binding.startTimeSpinner
        sharedvm.freeStartTimes.observe(viewLifecycleOwner) { newST ->
            val arrayAdapterST =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, newST)
            startTSpinner.adapter = arrayAdapterST

            val startTimeDefault =
                arrayAdapterST.getPosition(sharedvm.selStartTime!!.value)
            startTSpinner.setSelection(startTimeDefault)

            startTSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    sharedvm.setStartTime(newST[position] )
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        }



        val endTSpinner = binding.endTimeSpinner
        sharedvm.freeEndTimes.observe(viewLifecycleOwner) { newET ->
            val arrayAdapterET =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, newET)
            endTSpinner.adapter = arrayAdapterET


            val endTimeDefault =
                arrayAdapterET.getPosition(sharedvm.selEndTime!!.value)
            endTSpinner.setSelection(endTimeDefault)

            endTSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    sharedvm.setEndTime(newET[position] )
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        }





        val courtSportIdMap =  mutableMapOf<String,Int>()
        for (i in sharedvm.courts.value!!){
            courtSportIdMap[i.sport] = i.courtId

        }

        val sportTypeSpinner = binding.sportTypeSpinner
        val arrayAdapterSportType = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,sharedvm.sportList )
        sportTypeSpinner.adapter = arrayAdapterSportType
        val sport = courtSportIdMap.entries.find { it.value == sharedvm.selCourtId.value }?.key
        val sportTypeDefault = arrayAdapterSportType.getPosition(sport)
        sportTypeSpinner.setSelection(sportTypeDefault)
        sportTypeSpinner.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
//                var newCourtId = courtSportIdMap[sharedvm.sportList[position]]!!
                sharedvm.setSport(sharedvm.sportList[position])
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }





//new sport selected->  courtId showed will changed correspondingly
        val courtNameSpinner = binding.courtNameSpinner
        sharedvm.selSport.observe(viewLifecycleOwner) { newSport ->
            val courtNameIdMap = sharedvm.getCourtNameIdBySport(newSport,application = requireActivity().application)
            val courtNameList = courtNameIdMap.keys.toTypedArray()



            val arrayAdapterCourtName =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, courtNameList )
            courtNameSpinner.adapter = arrayAdapterCourtName

            val courtNameDefault = arrayAdapterCourtName.getPosition(sharedvm.selectedRes.value!!.name)
            courtNameSpinner.setSelection(courtNameDefault)

            courtNameSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    var newCourtId = courtNameIdMap[courtNameList[position]]!!
                    sharedvm.setCourtId(newCourtId)
                    //val sport = courtSportIdMap.entries.find { it.value == newCourtId }?.key
                    //sharedvm.setSport(sport!!)
                    sharedvm.getAllFreeSLotByCourtIdAndDate(newCourtId, sharedvm.selDate.value!!,0, application = activity!!.application )
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        }

//new courtName selected->  sport showed will changed correspondingly


//        val arrayAdapterSportType = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,sharedvm.sportList )
//        sportTypeSpinner.adapter = arrayAdapterSportType
//        val sportTypeDefault = arrayAdapterSportType.getPosition(sharedvm.selectedRes.value!!.sport)
//        sportTypeSpinner.setSelection(sportTypeDefault)
//        sportTypeSpinner.onItemSelectedListener = object : OnItemSelectedListener{
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
//                var newCourtId = courtSportIdMap[sharedvm.sportList[position]]!!
//                sharedvm.setSport(sharedvm.sportList[position])
//                sharedvm.setCourtId(newCourtId)
//                sharedvm.getAllFreeSLotByCourtIdAndDate(newCourtId, sharedvm.selDate.value!!,0, application = activity!!.application )
//            }
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//            }
//        }




        binding.apply {
            vm = sharedvm
            btnTimePicker.setOnClickListener{
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
            }
        }
    }
}