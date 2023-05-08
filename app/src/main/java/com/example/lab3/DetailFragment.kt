package com.example.lab3

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.InverseMethod
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentDetailBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

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

        sharedvm.selectedRes.observe(viewLifecycleOwner, { newRes ->

            binding!!.editStartTime.setText(newRes.startTime.toString())
            binding!!.editEndTime.setText(newRes.endTime.toString())
            binding!!.showDate.setText(newRes.date.toString())

        })

        binding!!.delBtn.setOnClickListener {
            sharedvm.deleteRes(sharedvm.resIdvm.value!!,this.requireActivity().application)
            findNavController().navigate(R.id.action_detailFragment_to_calendar)
            Toast.makeText(context, "Delete successfully", Toast.LENGTH_LONG).show()
        }
//        binding!!.AddBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_detailFragment_to_addFragment)
//           // Toast.makeText(context, "Add successfully", Toast.LENGTH_LONG).show()
//        }
        binding!!.saveBtn.setOnClickListener {
            sharedvm.addOrUpdateRes(this.requireActivity().application)
            findNavController().navigate(R.id.action_detailFragment_to_calendar)
            Toast.makeText(context, "Update successfully", Toast.LENGTH_LONG).show()


        }


        return fragmentBinding.root
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            // Specify the fragment as the lifecycle owner
//            lifecycleOwner = viewLifecycleOwner
            vm = sharedvm
            // Assign the view model to a property in the binding class

            btnTimePicker.setOnClickListener{

                //findNavController().navigate(R.id.action_detailFragment_to_timePickerFragment)
                //openTimePicker()


                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                // we have to implement setFragmentResultListener
                // show
                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")



            }

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment DetailFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            DetailFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}