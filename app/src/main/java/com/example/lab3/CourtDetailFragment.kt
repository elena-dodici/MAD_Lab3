//package com.example.lab3
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.core.view.isVisible
//import androidx.fragment.app.activityViewModels
//import androidx.navigation.fragment.findNavController
//import com.example.lab3.databinding.FragmentDetailBinding
//
//
//
///**
// * A simple [Fragment] subclass.
// * Use the [CourtDetailFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class CourtDetailFragment : BaseFragment(R.layout.fragment_court_detail) {
//
//    private lateinit var binding: FragmentDetailBinding
//    private val sharedvm : CourtViewModel by activityViewModels()
//
//
//
////    override fun onCreateView(
////        inflater: LayoutInflater, container: ViewGroup?,
////        savedInstanceState: Bundle?
////    ): View? {
////        // Inflate the layout for this fragment
////        return inflater.inflate(R.layout.fragment_court_detail, container, false)
////
////    }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding = FragmentDetailBinding.bind(view, savedInstanceState)
////        if (sharedvm.hasRev.value!!){
////            binding.delBtn.visibility = View.VISIBLE;
////        }
////        else binding.delBtn.visibility = View.GONE
//
//        binding.delBtn.setOnClickListener {
//            //if delete bottom show, it must means has review
//           // sharedvm.deleteCourtRev(this.requireActivity().application)
//            findNavController().navigate(R.id.action_courtDetailFragment_to_courtFragment)
//            Toast.makeText(context, "Delete successfully", Toast.LENGTH_LONG).show()
//        }
//        binding.saveBtn.setOnClickListener {
//
////               sharedvm.addOrUpdateCourtRev(this.requireActivity().application)
//                findNavController().navigate(R.id.action_courtDetailFragment_to_courtFragment)
//                Toast.makeText(context, "Update successfully", Toast.LENGTH_LONG).show()
//
//
//        }
//        binding.calBtn.setOnClickListener {
//            findNavController().navigate(R.id.action_courtDetailFragment_to_courtFragment)
//            Toast.makeText(context, "Your Review is not saved!", Toast.LENGTH_LONG).show()
//        }
//
//    }
//
//
//
//}

package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
class CourtDetailFragment : BaseFragment(R.layout.fragment_court_detail) {

    private lateinit var binding: FragmentCourtDetailBinding
    private val sharedvm: CourtViewModel by activityViewModels()


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

        sharedvm.courtRate.observe(viewLifecycleOwner){
                newCR ->
            binding.showCourtRate.text = newCR.toString()
        }




        if (sharedvm.hasRev.value!!) {
            binding.delBtn.visibility = View.VISIBLE;
        } else binding.delBtn.visibility = View.GONE

        binding.calBtn.setOnClickListener {
            findNavController().navigate(R.id.action_courtDetailFragment_to_courtFragment)
            Toast.makeText(context, "Your Review is not saved!", Toast.LENGTH_LONG).show()
        }

        binding.delBtn.setOnClickListener {
            //if delete bottom show, it must means has review
            sharedvm.deleteCourtRev(this.requireActivity().application)
            findNavController().navigate(R.id.action_courtDetailFragment_to_courtFragment)
            Toast.makeText(context, "Delete successfully", Toast.LENGTH_LONG).show()
        }
        binding.saveBtn.setOnClickListener {

               sharedvm.addOrUpdateCourtRev(4, sharedvm.review.value!!, this.requireActivity().application)
            findNavController().navigate(R.id.action_courtDetailFragment_to_courtFragment)
            Toast.makeText(context, "Update successfully", Toast.LENGTH_LONG).show()
        }
    }
}