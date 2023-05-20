package com.example.lab3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.database.entity.SportDetail
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment(R.layout.fragment_profile) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private  var full_name :String? = null
    private  var _name :String? = null
    private  var _surname :String? = null
    private  var tele :String? = null
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
        vm.getUserById(this.requireActivity().application,1)
        vm.getUserSportsById(this.requireActivity().application,1)
//        vm.User.observe(viewLifecycleOwner){
//            println("user:"+ (vm.User.value?.surname ))
//        }
        return inflater.inflate(R.layout.fragment_profile, container, false)
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
        val editButton = view.findViewById<Button>(R.id.btE)
        val fullName = view.findViewById<TextView>(R.id.tv_name)
        val tel = view.findViewById<TextView>(R.id.tv_phone)
        // 找到 RecyclerView 实例
        val recyclerViewSports = view.findViewById<RecyclerView>(R.id.recyclerViewS)

        // 设置布局管理器
        recyclerViewSports.layoutManager = LinearLayoutManager(requireContext())

        // 创建适配器并设置给 RecyclerView
        val adapter = SportsAdapter(vm.userSports.value ?: emptyList())

        recyclerViewSports.adapter = adapter
        adapter.setOnItemClickListener {
            var bundle = bundleOf("sportName" to it)
            findNavController().navigate(R.id.action_profileFragment_to_sportsDetail,bundle)
        }
        println(vm.userSports.value)
        vm.User.observe(viewLifecycleOwner){
            _name=it.name
            _surname=it.surname
            full_name="${it.name}"+"  "+"${it.surname}"
            tele = it.tel
            fullName.setText(full_name)
            tel.setText(tele)
//            println("user:"+ it)
        }
//        vm.userSports.observe(viewLifecycleOwner){

//        }
        editButton.setOnClickListener {
            var bundle = bundleOf("name" to _name,"surname" to _surname,"phone" to tele)
            println("button pressed")
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment,bundle)
        }
    }

}
class SportsAdapter(private val sportsList: List<SportDetail>) : RecyclerView.Adapter<SportsAdapter.SportsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_my_sports, parent, false)
        return SportsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SportsViewHolder, position: Int) {
        holder.bind(sportsList[position].sportType,sportsList[position].masteryLevel)
//        holder.itemView.setOnClickListener {
//            val selectedSport = sportsList[position].sportType
//            var bundle = bundleOf("sportName" to selectedSport)
//        }
    }

    override fun getItemCount(): Int {
        return sportsList.size
    }
    private var onItemClickListener: ((String) -> Unit)? = null
    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    inner class SportsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewSport: TextView = itemView.findViewById(R.id.item_text_sportName)
        private val textViewLevel: TextView = itemView.findViewById(R.id.item_text_materLevel)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // 处理点击事件
                    val selectedSport = sportsList[position].sportType
                    onItemClickListener?.invoke(selectedSport)
                }
            }
        }
        fun bind(sport: String,level:Int) {
            textViewSport.text = sport
            textViewLevel.text = level.toString()
        }
    }
}
