package com.example.lab3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lab3.database.entity.SportDetail
import com.example.lab3.database.entity.User

class EditProfileFragment: Fragment(R.layout.fragment_profile_edit) {

    private  var full_name :String? = null
    private  var _name :String? = null
    private  var _surname :String? = null
    private  var tele :String? = null
    private val vm : ProfileViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }

    } override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_edit, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        _name = arguments?.getString("name")
        _surname = arguments?.getString("surname")
        tele = arguments?.getString("phone")
        var sports = arguments?.getString("sports")
        val cancelButton = view.findViewById<Button>(R.id.btC)
        val saveButton = view.findViewById<Button>(R.id.btS)
        val bskball =view.findViewById<Button>(R.id.btbasketball)
        val editName = view.findViewById<EditText>(R.id.ed_name)
        val editSurname = view.findViewById<EditText>(R.id.ed_surname)
        val editTel = view.findViewById<EditText>(R.id.ed_phone)
        var SportDetail = SportDetail(1,0,"")
//        println(_name)
        editName.setText(_name)
        editSurname.setText(_surname)
        editTel.setText(tele)
        bskball.setOnClickListener {
            vm.addUserSport(this.requireActivity().application,SportDetail)
        }
        //cancelButton
        cancelButton.setOnClickListener {
            var bundle = bundleOf("name" to _name,"surname" to _surname,"phone" to tele)
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment,bundle)
        }
        //按下save将数据保存 TODO
        saveButton.setOnClickListener {
            val u = User(editName.text.toString(),editSurname.text.toString(),editTel.text.toString())
            if (u != null) {
                vm.updateUser(this.requireActivity().application,u,1)
            }
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
        }
    }
}