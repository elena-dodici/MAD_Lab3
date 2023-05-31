package com.example.lab3

import android.os.Bundle
import android.text.format.Time
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.*
import android.widget.AdapterView.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentDetailBinding

import java.time.LocalTime


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : BaseFragment(R.layout.fragment_detail), HasToolbar  {

    private lateinit var binding: FragmentDetailBinding
    private val sharedvm : CalendarViewModel by activityViewModels()
    private val mainvm: MainViewModel by activityViewModels()
    override val toolbar: Toolbar?
        get() = binding.activityToolbar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val fragmentBinding = FragmentDetailBinding.inflate(inflater, container, false)
//        binding = fragmentBinding
        binding = FragmentDetailBinding.bind(view, savedInstanceState)

        binding.delBtn.setOnClickListener {
            sharedvm.deleteRes(mainvm.user, sharedvm.selectedRes.value!!.resId)
            findNavController().navigate(R.id.action_detailFragment_to_calendar)
            mainvm.setShowNav(true)
            Toast.makeText(context, "Delete successfully", Toast.LENGTH_LONG).show()
        }
        binding.saveBtn.setOnClickListener {
            //add limitation for rating and review
            sharedvm.addOrUpdateRes(mainvm.user)
            findNavController().navigate(R.id.action_detailFragment_to_calendar)
            mainvm.setShowNav(true)
            Toast.makeText(context, "Update successfully", Toast.LENGTH_LONG).show()


        }
        binding.calBtn.setOnClickListener {
            findNavController().navigate(R.id.action_detailFragment_to_calendar)
            mainvm.setShowNav(true)
            Toast.makeText(context, "Unsaved information", Toast.LENGTH_LONG).show()
        }


        sharedvm.selDate.observe(viewLifecycleOwner){
            newSelDate ->
            binding.showDate.text = newSelDate.toString()
           //sharedvm.getFreeSlotByCourtName(sharedvm.selCourtName.value!!)

        }

//        val startTSpinner = binding.startTimeSpinner
//        sharedvm.freeStartTimes.observe(viewLifecycleOwner) { newST ->
//            newST.sorted()
//            val arrayAdapterST =
//                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, newST)
//            startTSpinner.adapter = arrayAdapterST
//
//            val startTimeDefault =
//                arrayAdapterST.getPosition(sharedvm.selStartTime!!.value)
//            startTSpinner.setSelection(startTimeDefault)
//
//            startTSpinner.onItemSelectedListener = object : OnItemSelectedListener {
//                override fun onItemSelected(
//                    p0: AdapterView<*>?,
//                    p1: View?,
//                    position: Int,
//                    p3: Long
//                ) {
//                    sharedvm.setStartTime(newST[position] )
//                }
//
//                override fun onNothingSelected(p0: AdapterView<*>?) {
//                }
//
//            }
//        }

        val avaTSpinner = binding.avaTimeSpinner
        sharedvm.freeStartTimes.observe(viewLifecycleOwner) { newST ->
            var isFirstSelection = true
            newST.sorted()
            var showList = mutableListOf<String>()
            for (i in newST){
                var time =LocalTime.of(i.hours, i.minutes, i.seconds)
                var newTime  = time.plusHours(1)

                val showtime = java.sql.Time(newTime.hour, newTime.minute, newTime.second)

                showList.add("$i - $showtime" )
            }
            val arrayAdapterST =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, showList)
            avaTSpinner.adapter = arrayAdapterST
            var sT = sharedvm.selStartTime!!.value!!

            var time =LocalTime.of(sT.hours, sT.minutes, 0)
            var newTime  = time.plusHours(1)
            val showtime = java.sql.Time(newTime.hour, newTime.minute, newTime.second)

            var default = "$sT - $showtime "
            println("default $sT")
            val startTimeDefault =
                arrayAdapterST.getPosition(default)
            avaTSpinner.setSelection(startTimeDefault)

            avaTSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                   if (isFirstSelection) {
                        isFirstSelection = false
                        return
                    }
                    //sharedvm.setStartTime(newST[position] )
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        }



        val courtNameSpinner = binding.courtNameSpinner
        sharedvm.getCourtNames()
        sharedvm.courtNames.observe(viewLifecycleOwner) { newCN ->
            var isFirstSelection = true
            val courtNameList = newCN
            val arrayAdapterCourtName =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    courtNameList
                )
            courtNameSpinner.adapter = arrayAdapterCourtName
            val courtNameDefault =
                arrayAdapterCourtName.getPosition(sharedvm.selectedRes.value!!.name)
            courtNameSpinner.setSelection(courtNameDefault)
            courtNameSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
//                    if (isFirstSelection) {
//                        isFirstSelection = false
//                        return
//                    }
                    var newCourtName = courtNameList[position]
                    sharedvm.setCourtName(newCourtName)
                    println("newcournamechange: ${sharedvm.selCourtName.value}")
                    //val sport = courtSportIdMap.entries.find { it.value == newCourtId }?.key
                    //sharedvm.setSport(sport!!)
                    sharedvm.getFreeSlotByCourtName(newCourtName)
                    //sharedvm.getAllFreeSLotByCourtIdAndDate(newCourtId, sharedvm.selDate.value!!,0, application = activity!!.application )
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        }


//        val courtSportIdMap =  mutableMapOf<String,Int>()
//        for (i in sharedvm.courts.value!!){
//            courtSportIdMap[i.sport] = i.courtId
//
//        }
//
//        val sportTypeSpinner = binding.sportTypeSpinner
//        sharedvm.selCourtName.observe(viewLifecycleOwner) {
//        val arrayAdapterSportType = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,sharedvm.sportList )
//        sportTypeSpinner.adapter = arrayAdapterSportType
//        val sport = courtSportIdMap.entries.find { it.value == sharedvm.selCourtId.value }?.key
//        val sportTypeDefault = arrayAdapterSportType.getPosition(sport)
//        sportTypeSpinner.setSelection(sportTypeDefault)
//        sportTypeSpinner.onItemSelectedListener = object : OnItemSelectedListener{
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
////                var newCourtId = courtSportIdMap[sharedvm.sportList[position]]!!
//                sharedvm.setSport(sharedvm.sportList[position])
//            }
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//            }
//        }


//new sport selected->  courtId showed will changed correspondingly
//        val courtNameSpinner = binding.courtNameSpinner
//        sharedvm.selSport.observe(viewLifecycleOwner) { newCN ->
//            val courtNameList = sharedvm.getCourtNames()
//
//
//
//            val arrayAdapterCourtName =
//                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, courtNameList )
//            courtNameSpinner.adapter = arrayAdapterCourtName
//
//            val courtNameDefault = arrayAdapterCourtName.getPosition(sharedvm.selectedRes.value!!.name)
//            courtNameSpinner.setSelection(courtNameDefault)
//
//            courtNameSpinner.onItemSelectedListener = object : OnItemSelectedListener {
//                override fun onItemSelected(
//                    p0: AdapterView<*>?,
//                    p1: View?,
//                    position: Int,
//                    p3: Long
//                ) {
//                    var newCourtName= courtNameList[position]
////                    sharedvm.setCourtId(newCourtId)
//                    //val sport = courtSportIdMap.entries.find { it.value == newCourtId }?.key
//                    //sharedvm.setSport(sport!!)
//                    //sharedvm.getAllFreeSLotByCourtIdAndDate(newCourtId, sharedvm.selDate.value!!,0, application = activity!!.application )
//                }
//
//                override fun onNothingSelected(p0: AdapterView<*>?) {
//                }
//
//            }
//        }

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