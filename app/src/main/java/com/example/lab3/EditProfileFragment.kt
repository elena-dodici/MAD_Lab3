package com.example.lab3

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.lab3.database.entity.SportDetail
import com.example.lab3.databinding.FragmentProfileEditBinding
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class EditProfileFragment: BaseFragment(R.layout.fragment_profile_edit), HasBackButton {

    private lateinit var binding: FragmentProfileEditBinding
//    override val toolbar: Toolbar?
//        get() = binding.activityToolbar
    override val titleRes: Int = R.string.profile
    private  var _name :String? = null
    private  var _surname :String? = null
    private  var tele :String? = null
    private val vm : ProfileViewModel by activityViewModels()
    private val vmMain : MainViewModel by activityViewModels()
    var frame: ImageView? = null
    var imageBitmap : Bitmap?  = null
    var hasPhotoChanged : Boolean = false
    var profilePicturePath : String? = null

    private var _sportList = listOf("running", "basketball", "swimming","tennis","pingpong")
    private  var SportDetail = mutableMapOf<String,ProfileViewModel.SportDetail?>()
    private  var DELETESportDetail = mutableMapOf<String,ProfileViewModel.SportDetail?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        _sportList.forEach{
            SportDetail[it]=null
        }
        _sportList.forEach{
            DELETESportDetail[it]=null
        }
        vm.userSports.value?.forEach{
            SportDetail[it.sportType]=it
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
    var image_uri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    val IMAGE_CAPTURE_CODE = 654
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentProfileEditBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        profilePicturePath = arguments?.getString("path")
        _name = arguments?.getString("name")
        _surname = arguments?.getString("surname")
        tele = arguments?.getString("phone")
        var img = view.findViewById<ImageView>(R.id.imageViewE)
        println("this _"+profilePicturePath)
        if(!profilePicturePath.isNullOrEmpty()){
            img.setImageURI(Uri.parse(profilePicturePath))
            Glide.with(this)
                .load(profilePicturePath)
                .override(img.width, img.height)
                .into(img)
        }


//        if(profilePicturePath != "NAN" && profilePicturePath != null && hasPhotoChanged == false ) {
//            println("LOADING PROFILE PICTURE FROM : $profilePicturePath")
//            loadImageFromStorage(profilePicturePath,img)
//        }
        val cancelButton = view.findViewById<Button>(R.id.btC)
        val saveButton = view.findViewById<Button>(R.id.btS)
//        val bskbt =view.findViewById<Button>(R.id.btbasketball)
//        val swbt = view.findViewById<Button>(R.id.btswimming)
//        val pingbt = view.findViewById<Button>(R.id.btpingpong)
//        val tennisbt = view.findViewById<Button>(R.id.bttennis)
//        val runbt = view.findViewById<Button>(R.id.btrunning)
        val editName = view.findViewById<EditText>(R.id.ed_name)
        val editSurname = view.findViewById<EditText>(R.id.ed_surname)
        val editTel = view.findViewById<EditText>(R.id.ed_phone)
        frame = view.findViewById(R.id.imageViewE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.context?.let { checkSelfPermission(it,Manifest.permission.CAMERA) } == PackageManager.PERMISSION_DENIED )
            {
                val permission = arrayOf(Manifest.permission.CAMERA)
                requestPermissions(permission, 112)
            }
        }
        val imagebtn = view.findViewById<ImageButton>(R.id.imgbtn)
        imagebtn.setOnClickListener{
            val popupMenu = PopupMenu(this.context,imagebtn)
            popupMenu.menuInflater.inflate(R.menu.camera_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                override fun onMenuItemClick(item: MenuItem): Boolean {
                    return when(item.itemId){
                        R.id.camera -> {

                            //captue image using camera
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (context?.let { it1 -> checkSelfPermission(it1,Manifest.permission.CAMERA) } == PackageManager.PERMISSION_DENIED
                                ) {
                                    val permission = arrayOf(
                                        Manifest.permission.CAMERA,

                                        )
                                    requestPermissions(permission, 121)
                                } else {
                                    openCamera()
                                }
                            }
                            true
                        }
                        R.id.photos -> {

                            //chose image from gallery
                            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)
                            true
                        }
                        else->false
                    }
                }
            })
            popupMenu.show()
        }

        editName.setText(_name)
        editSurname.setText(_surname)
        editTel.setText(tele)

        //初始化按钮状态,如果对应sport已经添加则设为不可选中

        SportDetail.values.forEach(){
            if (it != null) {
                //val color = ContextCompat.getColor(requireContext(), R.color.grey)
                when(it.sportType){
                    "running"->{
//                        runbt.setBackgroundColor(color)
//                        runbt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24, 0, 0, 0);
                        binding.cbrun.isChecked = true
                    }

                    "basketball"->{
//                        bskbt.setBackgroundColor(color)
//                        bskbt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24, 0, 0, 0);
                        binding.cbbb.isChecked = true
                    }

                    "pingpong"->{
//                        pingbt.setBackgroundColor(color)
//                        pingbt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24, 0, 0, 0);
                        binding.cbpp.isChecked = true
                    }

                    "tennis"->{
//                        tennisbt.setBackgroundColor(color)
//                        tennisbt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24, 0, 0, 0);
                        binding.cbtennis.isChecked = true
                    }

                    "swimming"->{
//                        swbt.setBackgroundColor(color)
//                        swbt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24, 0, 0, 0);
                        binding.cbswim.isChecked = true
                    }
                }
            }

        }



        //5个添加运动按键
//        bskbt.setOnClickListener {
//            addSportBtn(bskbt,"basketball")
//
//        }
//        runbt.setOnClickListener {
//
//            addSportBtn( runbt,"running")
//        }
//        tennisbt.setOnClickListener {
//
//            addSportBtn(tennisbt,"tennis")
//        }
//        swbt.setOnClickListener {
//
//            addSportBtn(swbt,"swimming")
//        }
//        pingbt.setOnClickListener {
//
//            addSportBtn(pingbt,"pingpong")
//        }
        //cancelButton
        cancelButton.setOnClickListener {
            vmMain.setShowNav(true)
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
        }
        //按下save将数据保存
        saveButton.setOnClickListener {
            vmMain.setShowNav(true)
            if(imageBitmap == null && image_uri != null){
                vm.uploadPhoto(this.requireActivity().application, image_uri!!,vmMain.UID)
                //val bitmap = uriToBitmap(image_uri!!)
                val bitmap= frame?.drawable?.toBitmap()
                imageBitmap = bitmap;
            }
            if(imageBitmap != null) {
                // imageBitmap will be null the first time we try to edit the profile and will remain
                // null if we don't select a profile picture
                vm.uploadPhoto(this.requireActivity().application, image_uri!!,vmMain.UID)
                println("Saving new path for the new picture... ")
                //profilePicturePath = saveToInternalStorage(imageBitmap!!)
                profilePicturePath = saveToInternalStorage(frame?.drawable?.toBitmap()!!)
            }
            if (editor != null) {
                editor.putString("path", profilePicturePath)
                editor.apply()
            }

            val u = ProfileViewModel.UserProfile(
                editName.text.toString(),
                editSurname.text.toString(),
                editTel.text.toString(),
                "user${vmMain.UID}/images/user${vmMain.UID}.jpg"
            )
            if (u != null) {
                vm.updateUser(this.requireActivity().application,u,vmMain.UID)
            }

            var bundle = bundleOf("Path" to profilePicturePath)
            vmMain.setShowNav(true)

            SportDetail.forEach{
                if (it.value!=null){
                    Log.w("ProfileVM", "check sportdetail befoew save:${it.value} ")
                    vm.addUserSport(it.value!!)
                }
            }

            DELETESportDetail.forEach{
                if (it.value!=null){
                    //println("test deleteSportDet: ${it.value}")
                    vm.deleteUserSport(this.requireActivity().application,it.value!!)
                }
            }


            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment,bundle)
        }



        binding.cbbb.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            val sportType = "basketball"
            if (isChecked) {

                val sp = vm.setSportDetail(vmMain.UID,sportType,0,"")
                SportDetail[sportType]=sp
                DELETESportDetail[sportType]=null
            } else {
                SportDetail[sportType]=null
                vm.userSports.value?.forEach {vit->
                    if (vit.sportType==sportType){
                        DELETESportDetail[sportType]=vit
                    }
                }
            }
        })

        onclicklistener( binding.cbrun, "running")
        onclicklistener( binding.cbswim, "swimming")
        onclicklistener( binding.cbpp, "pingpong")
        onclicklistener( binding.cbtennis, "tennis")
    }

     fun onclicklistener(cb: CheckBox, sportType: String){
        cb.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
             val sportType = sportType
             if (isChecked) {
                 val sp = vm.setSportDetail(vmMain.UID,sportType,0,"")
                 SportDetail[sportType]=sp

                 DELETESportDetail[sportType]=null
             } else {
                 SportDetail[sportType]=null
                 vm.userSports.value?.forEach {vit->
                     if (vit.sportType==sportType){
                         DELETESportDetail[sportType]=vit
                     }
                 }
             }
         })
     }

    fun addSportBtn(bt:Button,sportType:String){
        if(SportDetail[sportType]==null){
            val color = ContextCompat.getColor(requireContext(), R.color.grey)
            bt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_24, 0, 0, 0);
            bt.setBackgroundColor(color)
            val sp = ProfileViewModel.SportDetail(vmMain.UID,sportType,0,"")
            SportDetail[sportType]=sp
            DELETESportDetail[sportType]=null
        }
        else{
            SportDetail[sportType]=null
            val color = ContextCompat.getColor(requireContext(), R.color.lighter_toolbar_color)
            bt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            bt.setBackgroundColor(color)
            vm.userSports.value?.forEach {vit->
                if (vit.sportType==sportType){
                    DELETESportDetail[sportType]=vit
                }
            }

//            println("${sportType} 空")
        }
    }


    //opens camera so that user can capture image
    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")

        image_uri = (activity as MainActivity?)!!.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }
    //takes URI of the image and returns bitmap
    private fun uriToBitmap(selectedFileUri: Uri): Bitmap? {
        try {
            val parcelFileDescriptor = (activity as MainActivity?)!!.contentResolver.openFileDescriptor(selectedFileUri, "r")
            val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    //last check and substitute the image + Save bitmap to save image into local memory

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            val bitmap = uriToBitmap(image_uri!!)
            imageBitmap = bitmap;
            frame?.setImageBitmap(bitmap)
            hasPhotoChanged = true
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            image_uri = data.data
            val bitmap = uriToBitmap(image_uri!!)
            imageBitmap = bitmap;
            frame?.setImageBitmap(bitmap)
            hasPhotoChanged = true
        }
    }
    private fun saveToInternalStorage(bitmapImage: Bitmap): String? {
        val cw = ContextWrapper(this.context)
        // path to /data/data/yourapp/app_data/photoDir
        val directory: File = cw.getDir("photoDir", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "profilePicture.jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return directory.getAbsolutePath()
    }

    private fun loadImageFromStorage(path: String?,img:ImageView) {
        try {
            val f = File(path, "profilePicture.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            img.setImageBitmap(b)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}

//确认提示弹窗
//    fun Dialog(bt:Button){
//        val builder = AlertDialog.Builder(context)
//        builder.setTitle("Add to Your Interested")
//        builder.setMessage("Are you sure you want to add this sport to your interested sports?")
//        builder.setPositiveButton("yes") { dialog, which ->
//            //点击确认的情况，添加对应数据到db并设置按钮不可选
//            vm.addUserSport(this.requireActivity().application,SportDetail)
//            bt.isEnabled
//        }
//        builder.setNegativeButton("no", null)
//        val dialog = builder.create()
//        dialog.show()
//    }