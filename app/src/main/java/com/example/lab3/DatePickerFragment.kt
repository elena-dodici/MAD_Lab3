package com.example.lab3

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import java.time.LocalDate
import java.util.*



class DatePickerFragment() : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val sharedvm : CalendarViewModel by activityViewModels()
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current time as the default values for the picker
            val year = sharedvm.selDate.value!!.year
            val month = sharedvm.selDate.value!!.monthValue
            val day = sharedvm.selDate.value!!.dayOfMonth
            // Use the current time as the default values for the picker
            return DatePickerDialog(requireContext(),this,year,month - 1 ,day)


        }


    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val newDate = LocalDate.of(year,month+1,day)
        sharedvm.setSelDate(newDate)
        sharedvm.getFreeSlotByCourtName(sharedvm.selCourtName.value!!)
//        sharedvm.getAllFreeSLotByCourtIdAndDate(sharedvm.selectedRes.value!!.courtId, newDate,0,this.requireActivity().application)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_picker, container, false)
    }

}