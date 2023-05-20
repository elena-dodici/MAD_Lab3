package com.example.lab3

import android.app.AlertDialog
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
    private var SportDetail = SportDetail(1,"running",0,"")

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
        val cancelButton = view.findViewById<Button>(R.id.btC)
        val saveButton = view.findViewById<Button>(R.id.btS)
        val bskbt =view.findViewById<Button>(R.id.btbasketball)
        val swbt = view.findViewById<Button>(R.id.btswimming)
        val pingbt = view.findViewById<Button>(R.id.btpingpong)
        val tennisbt = view.findViewById<Button>(R.id.bttennis)
        val runbt = view.findViewById<Button>(R.id.btrunning)
        val editName = view.findViewById<EditText>(R.id.ed_name)
        val editSurname = view.findViewById<EditText>(R.id.ed_surname)
        val editTel = view.findViewById<EditText>(R.id.ed_phone)


        editName.setText(_name)
        editSurname.setText(_surname)
        editTel.setText(tele)
        //初始化按钮状态,如果对应sport已经添加则设为不可选中
        vm.userSports.value?.forEach(){
            when(it.sportType){
                "running"->{
                    runbt.isEnabled=false
                }
                "basketball"->{
                    bskbt.isEnabled=false
                }
                "pingpong"->{
                    pingbt.isEnabled= false
                }
                "tennis"->{
                    tennisbt.isEnabled=false
                }
                "swimming"->{
                    swbt.isEnabled=false
                }
            }
        }
        //5个添加运动按键
        bskbt.setOnClickListener {
            SportDetail.sportType="basketball"
            Dialog(bskbt)
        }
        runbt.setOnClickListener {
            SportDetail.sportType="running"
            Dialog(runbt)
        }
        tennisbt.setOnClickListener {
            SportDetail.sportType="tennis"
            Dialog(tennisbt)
        }
        swbt.setOnClickListener {
            SportDetail.sportType="swimming"
            Dialog(swbt)
        }
        pingbt.setOnClickListener {
            SportDetail.sportType="pingpong"
            Dialog(pingbt)
        }
        //cancelButton
        cancelButton.setOnClickListener {
            var bundle = bundleOf("name" to _name,"surname" to _surname,"phone" to tele)
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment,bundle)
        }
        //按下save将数据保存
        saveButton.setOnClickListener {
            val u = User(editName.text.toString(),editSurname.text.toString(),editTel.text.toString())
            if (u != null) {
                vm.updateUser(this.requireActivity().application,u,1)
            }
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
        }
    }
    //确认提示弹窗
    fun Dialog(bt:Button){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add to Your Interested")
        builder.setMessage("Are you sure you want to add this sport to your interested sports?")
        builder.setPositiveButton("yes") { dialog, which ->
            //点击确认的情况，添加对应数据到db并设置按钮不可选
            vm.addUserSport(this.requireActivity().application,SportDetail)
            bt.isEnabled=false
        }
        builder.setNegativeButton("no", null)
        val dialog = builder.create()
        dialog.show()
    }
}