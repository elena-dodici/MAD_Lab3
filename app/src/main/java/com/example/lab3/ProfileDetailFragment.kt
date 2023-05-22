package com.example.lab3

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.database.entity.SportDetail
import com.example.lab3.database.entity.User
import java.util.UUID
import java.util.logging.Level

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileDetailFragment : Fragment(R.layout.fragment_profile_detail),HasToolbar {

    override val toolbar: Toolbar?
        get() = null
    private var param1: String? = null
    private var param2: String? = null
    private var sportName:String? = null
    private var achievement:String? = null
    private var Level:Int = 0
    private var SportDetail = SportDetail(1,"running",0,"")
    private val vm : ProfileViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vm.getUserSportsById(this.requireActivity().application,1)

        return inflater.inflate(R.layout.fragment_profile_detail, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val cancelButton = view.findViewById<Button>(R.id.btC)
        val saveButton = view.findViewById<Button>(R.id.btS)
        val deleteButton = view.findViewById<Button>(R.id.btD)
        val sportText = view.findViewById<EditText>(R.id.SportName)
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val achText = view.findViewById<EditText>(R.id.ed_achievement)
        //初始化数据
        sportName = arguments?.getString("sportName")
        vm.userSports.value?.forEach{
            if(it.sportType==sportName){
                Level = it.masteryLevel
                achievement = it.achievement
                SportDetail=it
        }
        }
        sportText.setText(sportName)
        //评分只可选择整数
        ratingBar.stepSize = 1f
        ratingBar.rating = Level.toFloat()
        achText.setText(achievement)
        //删除
        deleteButton.setOnClickListener {
            Dialog()
        }
        //按下save将数据保存
        saveButton.setOnClickListener {
            //将修改后的数值添加到数据库
            SportDetail.sportType= sportText.text.toString()
            SportDetail.masteryLevel= ratingBar.rating.toInt()
            SportDetail.achievement= achText.text.toString()
               vm.updateUserSport(this.requireActivity().application,SportDetail,1)
            findNavController().navigate(R.id.action_ProfileDetailFragment_to_profileFragment)
        }
        cancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_ProfileDetailFragment_to_profileFragment)
        }
    }
    //确认提示弹窗
    fun Dialog(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Are you sure you want to Delete?")
        builder.setMessage("Are you sure you want to remove this sport from your interested sports?")
        builder.setPositiveButton("yes") { dialog, which ->
            //点击确认的情况，添加对应数据到db并设置按钮不可选
            vm.deleteUserSport(this.requireActivity().application,SportDetail)
            //删除完毕后返回profile界面
            findNavController().navigate(R.id.action_ProfileDetailFragment_to_profileFragment)
        }
        builder.setNegativeButton("no", null)
        val dialog = builder.create()
        dialog.show()
    }

}
