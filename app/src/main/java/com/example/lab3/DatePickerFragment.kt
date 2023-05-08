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


/**
 * A simple [Fragment] subclass.
 * Use the [DatePickerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DatePickerFragment(val max: () -> Unit) : DialogFragment(), DatePickerDialog.OnDateSetListener {
    // TODO: Rename and change types of parameters

    private val sharedvm : CalendarViewModel by activityViewModels()
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current time as the default values for the picker
            val year = sharedvm.selectedRes.value!!.date.year
            val month = sharedvm.selectedRes.value!!.date.getMonthValue()
            val day = sharedvm.selectedRes.value!!.date.dayOfMonth
            // Use the current time as the default values for the picker
            return DatePickerDialog(requireContext(),this,year,month - 1 ,day)


        }


    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        val newDate = LocalDate.of(year,month+1,day)
        sharedvm.selectedRes.value!!.date = newDate
        max()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_picker, container, false)
    }

}