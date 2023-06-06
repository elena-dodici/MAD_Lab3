package com.example.lab3

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lab3.database.entity.SportDetail
import com.example.lab3.databinding.FragmentProfileDetailBinding
import com.example.lab3.databinding.FragmentSignUpBinding
import com.example.lab3.viewmodel.SignUpViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

class SignUpFragment : BaseFragment(R.layout.fragment_sign_up),HasBackButton {
    private lateinit var binding: FragmentSignUpBinding

    override val titleRes: Int = R.string.signup

    private val vm : SignUpViewModel by activityViewModels()
    private val vmMain : MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignUpBinding.bind(view)
        val emailText = view.findViewById<EditText>(R.id.editTextEmail)
        val nameText = view.findViewById<EditText>(R.id.editTextName)
        val surnameText = view.findViewById<EditText>(R.id.editTextSurname)
        val telText = view.findViewById<EditText>(R.id.editTel)
        val passwordText = view.findViewById<EditText>(R.id.editTextPassword)
        val saveButton = view.findViewById<Button>(R.id.buttonRegister)
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        saveButton.setOnClickListener{

            when{
                emailText.text.isNullOrEmpty() -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Email empty!")
                        .setMessage("Email address cannot be empty")
                        .setPositiveButton("OK", null)
                        .create()
                    alertDialog.show()
                }
                nameText.text.isNullOrEmpty() -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Name empty!")
                        .setMessage("Name cannot be empty")
                        .setPositiveButton("OK", null)
                        .create()
                    alertDialog.show()
                }
                surnameText.text.isNullOrEmpty() -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Surname empty")
                        .setMessage("Surname cannot be empty")
                        .setPositiveButton("OK", null)
                        .create()
                    alertDialog.show()
                }
                telText.text.isNullOrEmpty() -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Phone number empty")
                        .setMessage("Phone number can not be empty")
                        .setPositiveButton("OK", null)
                        .create()
                    alertDialog.show()
                }
                passwordText.text.isNullOrEmpty() -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Password empty")
                        .setMessage("Password cannot be empty")
                        .setPositiveButton("OK", null)
                        .create()
                    alertDialog.show()
                }
                passwordText.text.length < 6 -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Password invalid")
                        .setMessage("Password length must not less than 6")
                        .setPositiveButton("OK", null)
                        .create()
                    alertDialog.show()
                }
                else->{
                    if(emailText.text.toString().matches(emailPattern.toRegex())){
                        vm.signUp(this.requireActivity().application,emailText.text.toString(), passwordText.text.toString(),nameText.text.toString(),surnameText.text.toString(),telText.text.toString())
                            vm.operationResult.observe(viewLifecycleOwner) {
                                if (it==true){
                                    Toast.makeText(this.requireContext(), "Sign up success!.", Toast.LENGTH_SHORT).show()
                                    val alertDialog = AlertDialog.Builder(context)
                                        .setTitle("Almost done...")
                                        .setMessage("We'll send an email to ${emailText.text} in 2 minutes. Open it up to activate your account.")
                                    alertDialog.show()
                                    findNavController().navigate(R.id.to_loginFragment)
                                    //go to profile page and let it add their information!!!
                                }
                            }
                    }else{
                        val alertDialog = AlertDialog.Builder(context)
                            .setTitle("Email invalid")
                            .setMessage("Please input a valid email address")
                            .setPositiveButton("OK", null)
                            .create()
                        alertDialog.show()
                    }
                }
            }


        }
//        sButton.setOnClickListener{
//            findNavController().navigate(R.id.to_loginFragment)
//        }
    }



}
