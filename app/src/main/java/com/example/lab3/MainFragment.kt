package com.example.lab3

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lab3.databinding.FragmentMainBinding


class MainFragment : BaseFragment(R.layout.fragment_main), HasToolbar {
    private val vmMain : MainViewModel by activityViewModels()
    companion object {
        fun newInstance() = MainFragment()
    }
    private lateinit var binding: FragmentMainBinding
    override val toolbar: Toolbar?
        get() = binding.activityToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        val inputEmail = binding.editTextEmail.editText?.text
        val inputPassword = binding.editTextPassword.editText?.text
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"

        binding.editTextEmail.editText?.doOnTextChanged { inputText, _, _, _ ->
            // Respond to input text change
            println("change activate: ${inputText}")
        }

        binding.buttonSave.setOnClickListener {
//            println("I got inputEmail!: ${inputEmail.toString()}")
//            println("I got input password!: ${inputPassword.toString()}")


            when{
                inputEmail.toString().isNullOrEmpty() -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Email empty!")
                        .setMessage("Email address cannot be empty")
                        .setPositiveButton("OK", null)
                        .create()
                    alertDialog.show()
                }
                inputPassword.toString().isNullOrEmpty() -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Password empty !")
                        .setMessage("Password cannot be empty")
                        .setPositiveButton("OK", null)
                        .create()
                    alertDialog.show()
                }
                inputPassword.toString().length < 6 -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Password invalid")
                        .setMessage("Password length must not less than 6")
                        .setPositiveButton("OK", null)
                        .create()
                    alertDialog.show()
                }
                else->{
                    if(inputEmail.toString().matches(emailPattern.toRegex())){
                        vmMain.login(this.requireActivity().application,inputEmail.toString(), inputPassword.toString())
                        if(!vmMain.UID.isNullOrEmpty()){
                            Toast.makeText(this.requireContext(), "Login success!.", Toast.LENGTH_SHORT).show()
                            //go to profile page!!!!

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

        binding.register.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment2_to_SignUpFragment)
        }


    }
}