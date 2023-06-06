package com.example.lab3

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lab3.databinding.FragmentCourtDetailBinding
import com.example.lab3.databinding.FragmentProfileBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
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
class ProfileFragment : BaseFragment(R.layout.fragment_profile),HasToolbar {
    private lateinit var  binding: FragmentProfileBinding
    override val toolbar: Toolbar?
        get() = binding.activityToolbar
    override val titleRes: Int = R.string.profile
    private var param1: String? = null
    private var param2: String? = null
    private  var full_name :String? = null
    private  var _name :String? = null
    private  var _surname :String? = null
    private  var tele :String? = null
    private val vm : ProfileViewModel by activityViewModels()
    private val vmMain : MainViewModel by activityViewModels()
    private  var sportList : List<ProfileViewModel.SportDetail>? = null
    private  var path :String?=null

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
        vm.getUserById(this.requireActivity().application, vmMain.UID)
        vm.getUserSportsById(this.requireActivity().application, vmMain.UID)
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
        binding = FragmentProfileBinding.bind(view)
        // 找到 RecyclerView 实例
        val recyclerViewSports = view.findViewById<RecyclerView>(R.id.recyclerViewS)
//        val sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
//        val savedPath = sharedPreferences.getString("path", null)

        val users = listOf("user1", "user2", "user3")
//        val spinnerUser = view.findViewById<Spinner>(R.id.userSpinner)
//        spinnerUser.setSelection(vmMain.user-1)
        val adapterU = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, users)
        adapterU.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinnerUser.adapter = adapterU
        vm.userSports.observe(viewLifecycleOwner){
            sportList= it
//            println("this"+sportList)
            recyclerViewSports.layoutManager = LinearLayoutManager(requireContext())

            // 创建适配器并设置给 RecyclerView
            var adapter = SportsAdapter(sportList ?: emptyList())

            recyclerViewSports.adapter = adapter
            adapter.setOnItemClickListener {
                var bundle = bundleOf("sportName" to it)
                vmMain.setShowNav(false)
                findNavController().navigate(R.id.action_profileFragment_to_sportsDetail,bundle)
            }
        }

//        profilePicturePath = arguments?.getString("Path")
        super.onViewCreated(view, savedInstanceState)
//        loadImageFromStorage(profilePicturePath)
        val editButton = view.findViewById<Button>(R.id.btE)
        val historyButton = view.findViewById<Button>(R.id.btHis)
        val fullName = view.findViewById<TextView>(R.id.tv_name)
        val tel = view.findViewById<TextView>(R.id.tv_phone)
        val photoView = view.findViewById<ImageView>(R.id.TOP)
        val logout = view.findViewById<Button>(R.id.Logout)


        // 设置布局管理器
        recyclerViewSports.layoutManager = LinearLayoutManager(requireContext())

        // 创建适配器并设置给 RecyclerView
        var adapter = SportsAdapter(sportList ?: emptyList())

        recyclerViewSports.adapter = adapter
        adapter.setOnItemClickListener {
            var bundle = bundleOf("sportName" to it)
            vmMain.setShowNav(false)
            findNavController().navigate(R.id.action_profileFragment_to_sportsDetail,bundle)
        }

        vm.User.observe(viewLifecycleOwner){
            _name=it.name
            _surname=it.surname
            full_name="${it.name}"+"  "+"${it.surname}"
            tele = it.tel
            fullName.setText(full_name)
            tel.setText(tele)
            if(!it.photo.isNullOrEmpty())
            {  vm.readPhoto(this.requireActivity().application, it.photo!!) }
        }

        //读取头像URI加载到imageview
        vm.photoUri.observe(viewLifecycleOwner) {
            path=it
            Glide.with(this)
                .load(it)
                .into(photoView)
        }
//        println(vmMain.user)
//        spinnerUser.setSelection(vmMain.user-1)
//        spinnerUser.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
//                updateUser(parent.getItemAtPosition(position).toString())
//                vm.User.observe(viewLifecycleOwner){
//                    _name=it.name
//                    _surname=it.surname
//                    full_name="${it.name}"+"  "+"${it.surname}"
//                    tele = it.tel
//                    fullName.setText(full_name)
//                    tel.setText(tele)
//                    vmMain.user = (position + 1)
//                    println("this is new user position !!! ${position}")
//                }
//                adapter = SportsAdapter(sportList ?: emptyList())
//                recyclerViewSports.adapter = adapter
//                adapter.setOnItemClickListener {
//                    var bundle = bundleOf("sportName" to it)
//                    vmMain.setShowNav(false)
//                    findNavController().navigate(R.id.action_profileFragment_to_sportsDetail,bundle)
//                }
//            }
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
        //需把action_profileFragment_to_historyFragment的destination改成正确的跳转目的地
        historyButton.setOnClickListener{
            vmMain.setShowNav(false)
            findNavController().navigate(R.id.action_profileFragment_to_historyFragment)
        }
        //登出
        logout.setOnClickListener{
            vmMain.logOut(this.requireActivity().application)
            vmMain.setShowNav(false)
            findNavController().navigate(R.id.action_profileFragment_to_main)
        }
        editButton.setOnClickListener {
            var bundle = bundleOf("name" to _name,"surname" to _surname,"phone" to tele,"path" to path)

            vmMain.setShowNav(false)
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment,bundle)
        }
    }

//    private fun updateUser(User: String) {
//        when(User){
//            ("user1")->{
//                vmMain.user = 1
//            }
//            ("user2")->{
//                vmMain.user = 2
//            }
//            ("user3")->{
//                vmMain.user = 3
//            }
//
//        }
//        vm.getUserById(this.requireActivity().application, vmMain.user)
//        vm.getUserSportsById(this.requireActivity().application, vmMain.user)
//
//    }

    private fun loadImageFromStorage(path: String?) {
        try {
            val f = File(path, "profilePicture.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            val img: ImageView = requireView().findViewById<ImageView>(R.id.imageView)
            img.setImageBitmap(b)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}
class SportsAdapter(private val sportsList: List<ProfileViewModel.SportDetail>) : RecyclerView.Adapter<SportsAdapter.SportsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SportsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_my_sports, parent, false)
        return SportsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SportsViewHolder, position: Int) {
        sportsList[position].masteryLevel?.let { holder.bind(sportsList[position].sportType, it) }
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
    private fun refreshFragment(view: View) {

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
        fun bind(sport: String,level:Long) {
            textViewSport.text = sport
            textViewLevel.text = level.toString()
        }
    }
}
