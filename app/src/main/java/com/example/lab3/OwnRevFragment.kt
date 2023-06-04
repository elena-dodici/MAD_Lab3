package com.example.lab3

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentOwnRevBinding

import java.time.LocalDateTime


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [OwnRevFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OwnRevFragment : BaseFragment(R.layout.fragment_own_rev), HasToolbar {

    private lateinit var binding: FragmentOwnRevBinding
    private val sharedvm: HistoryViewModel by activityViewModels()
    private val mainvm: MainViewModel by activityViewModels()
    override val toolbar: Toolbar?
        get() = binding.activityToolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOwnRevBinding.bind(view, savedInstanceState)

        binding.calBtn.setOnClickListener {
            gobackCal("Back to previous page")
        }

        val ratingBar = binding.ratingBar
        binding.saveBtn.setOnClickListener {
            //add limitation for rating and review

            sharedvm.addRev(ratingBar.rating.toInt(), mainvm.user)
            gobackCal("Update successfully")

        }

        binding.apply {
            vm1 = sharedvm
        }

    }



    fun gobackCal(message: String){
        findNavController().navigate(R.id.action_ownRevFragment_to_historyFragment)
        mainvm.setShowNav(false)
        //Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}