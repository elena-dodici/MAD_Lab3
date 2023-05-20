package com.example.lab3

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
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
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lab3.database.entity.SportDetail
import com.example.lab3.database.entity.User
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class EditProfileFragment: BaseFragment(R.layout.fragment_profile_edit), HasToolbar {

    override val toolbar: Toolbar?
        get() = null
    private  var _name :String? = null
    private  var _surname :String? = null
    private  var tele :String? = null
    private val vm : ProfileViewModel by activityViewModels()
    private val mainvm: MainViewModel by activityViewModels()
    var frame: ImageView? = null
    var imageBitmap : Bitmap?  = null
    var hasPhotoChanged : Boolean = false
    var profilePicturePath : String? = null
    private var SportDetail = SportDetail(1,"running",0,"")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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
        val sharedPreferences = context?.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        _name = arguments?.getString("name")
        _surname = arguments?.getString("surname")
        tele = arguments?.getString("phone")
        profilePicturePath = arguments?.getString("Path")
        var img = view.findViewById<ImageView>(R.id.imageViewE)
        if(profilePicturePath != "NAN" && profilePicturePath != null && hasPhotoChanged == false ) {
            println("LOADING PROFILE PICTURE FROM : $profilePicturePath")
            loadImageFromStorage(profilePicturePath,img)
        }
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
         mainvm.setShowNav(true)
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
        }
        //按下save将数据保存
        saveButton.setOnClickListener {
            if(imageBitmap == null && image_uri != null){
                //val bitmap = uriToBitmap(image_uri!!)
                val bitmap= frame?.drawable?.toBitmap()
                imageBitmap = bitmap;
            }
            if(imageBitmap != null) {
                // imageBitmap will be null the first time we try to edit the profile and will remain
                // null if we don't select a profile picture
                println("Saving new path for the new picture... ")
                //profilePicturePath = saveToInternalStorage(imageBitmap!!)
                profilePicturePath = saveToInternalStorage(frame?.drawable?.toBitmap()!!)
            }
            if (editor != null) {
                editor.putString("path", profilePicturePath)
                editor.apply()
            }

            val u = User(editName.text.toString(),editSurname.text.toString(),editTel.text.toString())
            if (u != null) {
                vm.updateUser(this.requireActivity().application,u,1)
            }
            var bundle = bundleOf("Path" to profilePicturePath)
            mainvm.setShowNav(true)
            findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment,bundle)
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
    var image_uri: Uri? = null
    private val RESULT_LOAD_IMAGE = 123
    val IMAGE_CAPTURE_CODE = 654

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