package com.example.lab3

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentDetailBinding
import java.time.*


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : BaseFragment(R.layout.fragment_detail), HasBackButton  {

    private lateinit var binding: FragmentDetailBinding
    private val sharedvm : CalendarViewModel by activityViewModels()
    private val mainvm: MainViewModel by activityViewModels()
//    private var Level:Int = 0
//    override val toolbar: Toolbar?
//        get() = binding.activityToolbar
    override val titleRes: Int = R.string.reservation_detail
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val fragmentBinding = FragmentDetailBinding.inflate(inflater, container, false)
//        binding = fragmentBinding
        binding = FragmentDetailBinding.bind(view, savedInstanceState)

        binding.delBtn.setOnClickListener {

            Dialog("DELETE")

        }
        //val resTimeInitialValue =
        //    LocalDateTime.of(sharedvm.selDate.value!!.year, sharedvm.selDate.value!!.monthValue, sharedvm.selDate.value!!.dayOfMonth, sharedvm.selectedRes.value!!.startTime.hours, sharedvm.selectedRes.value!!.startTime.minutes)
        //val resTimeLiveData = sharedvm.selDate

        /*resTimeLiveData.observe(viewLifecycleOwner) {
            println("DAY : ${resTimeLiveData.value!!.dayOfMonth} - MONTH : ${resTimeLiveData.value!!.month} " +
                    "- YEAR : ${resTimeLiveData.value!!.year} ")
        }

        hour.observe(viewLifecycleOwner){
            println("HOUR : ${hour.value!!.hours} - MINUTES : ${hour.value!!.minutes}")
        }*/


        binding.saveBtn.setOnClickListener {
            //add limitation for rating and review

            val resTime = LocalDateTime.of(sharedvm.selDate.value!!.year, sharedvm.selDate.value!!.monthValue, sharedvm.selDate.value!!.dayOfMonth, sharedvm.selectedRes.value!!.startTime.hours, sharedvm.selectedRes.value!!.startTime.minutes)

            if (LocalDateTime.now().isAfter(resTime)  ){
                Toast.makeText(context, "You cannot change your reservation before today", Toast.LENGTH_LONG).show()
            }else{
                println("resTime variable >>>>>>>>>>>>> ${resTime}")
                Dialog("SAVE")
//                findNavController().navigate(R.id.action_detailFragment_to_calendar)
//                mainvm.setShowNav(true)
//                Toast.makeText(context, "Update successfully", Toast.LENGTH_LONG).show()
            }





        }
        binding.calBtn.setOnClickListener {
            gobackCal("Information Unsaved")
        }


        sharedvm.selDate.observe(viewLifecycleOwner){
            newSelDate ->
            binding.showDate.text = newSelDate.toString()
            sharedvm.getFreeSlotByCourtName(sharedvm.selCourtName.value!!)

        }




        val avaTSpinner = binding.avaTimeSpinner1
        sharedvm.freeStartTimes.observe(viewLifecycleOwner) { newST ->
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

            var default = "$sT - $showtime"

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

                    //transform from string into time
                    var sTstring = showList[position].split(" ")[0]
                    var putST = java.sql.Time(sTstring.split(":")[0].toInt(),0,0)
                    sharedvm.setStartTime(putST )

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }

            }
        }

//        sharedvm.combinedLiveData.addSource(sharedvm.selCourtName) { value1 ->
//            val value2 = sharedvm.courtNames.value
//            if (value2 != null) {
//                sharedvm.combinedLiveData.value = Pair(value1, value2)
//            }
//        }
//
//        sharedvm.combinedLiveData.addSource(sharedvm.courtNames) { value2 ->
//            val value1 = sharedvm.selCourtName.value
//            if (value1 != null) {
//                sharedvm.combinedLiveData.value = Pair(value1, value2)
//            }
//        }

        val courtNameSpinner = binding.courtNameSpinner
        var courtNameList = sharedvm.courtNamesSport.keys.toList().sorted()
        //sharedvm.selCourtName.observe(viewLifecycleOwner) { newSt->
//        println("test${courtNameList}")


            val arrayAdapterCourtName =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    courtNameList.sorted()
                )
            courtNameSpinner.adapter = arrayAdapterCourtName
            val courtNameDefault =
                arrayAdapterCourtName.getPosition(sharedvm.selectedRes.value!!.name)
            courtNameSpinner.setSelection(courtNameDefault)


//            (v as TextView).textSize = 5F
            courtNameSpinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    p0: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {

                    var newCourtName = courtNameList[position]
                    sharedvm.setSelCourtName(newCourtName)
                    sharedvm.setSelSport(sharedvm.courtNamesSport[newCourtName]!!)
                    sharedvm.getFreeSlotByCourtName(newCourtName)

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
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

    fun gobackCal(message: String){
        findNavController().navigate(R.id.action_detailFragment_to_calendar)
        mainvm.setShowNav(true)
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    private fun Dialog(buttonType : String) {
        var failed = false
        lateinit var message : String
        if (buttonType == "DELETE") {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete reservation")
            builder.setMessage("Your reservation will be cancelled! Confirm?")
            builder.setPositiveButton("Confirm") { dialog, which ->

                sharedvm.deleteRes(mainvm.UID, sharedvm.selectedRes.value!!.resId)

                gobackCal("Delete successfully")
            }
            builder.setNegativeButton("Cancel", null)
            val dialog = builder.create()
            dialog.setOnShowListener {
                val posBt = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                val negBt = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                posBt.setTextColor(resources.getColor(R.color.button_color))
                negBt.setTextColor(resources.getColor(R.color.md_theme_light_error))
            }
            dialog.show()
        } else {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton("Confirm",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User clicked OK button

                            try {
                                // Use call to db here
                                sharedvm.addOrUpdateRes(mainvm.UID)
                                sharedvm.test(mainvm.UID)
                            } catch (e: Exception) {
                                failed = true;
                                message = "Operation failed , retry later!"
                            }
                            if (failed == false) {
                                message = "Data updated with success"
                            }
                            // Navigate to the source fragment here
                            gobackCal(message)
                        })
                    setNegativeButton("Cancel",
                        DialogInterface.OnClickListener { dialog, id ->
                            // User cancelled the dialog
                        })
                    setTitle("Modify reservation")
                }
                // Create the AlertDialog
                builder.create()
            }
            alertDialog?.setMessage("Are you sure you want to modify this reservation?")
            alertDialog?.setOnShowListener {
                val positiveBtn = alertDialog?.getButton(AlertDialog.BUTTON_POSITIVE)
                val negativeBtn = alertDialog?.getButton(AlertDialog.BUTTON_NEGATIVE)
                positiveBtn?.setTextColor(resources.getColor(R.color.button_color))
                negativeBtn?.setTextColor(resources.getColor(R.color.md_theme_light_error))
            }
            alertDialog?.show()

        }
    }
}



