package com.example.lab3

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
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
    override val titleRes: Int = R.string.main_page
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmMain.setShowNav(false)
        binding = FragmentMainBinding.bind(view)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

//        val inputEmail = binding.editTextEmail.editText?.text
//        val inputPassword = binding.editTextPassword.editText?.text
        val emailText =  view.findViewById<EditText>(R.id.editTextEmail1)
        val passwordText = view.findViewById<EditText>(R.id.editTextPassword1)
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"

        binding.editTextEmail.editText?.doOnTextChanged { inputText, _, _, _ ->
            // Respond to input text change
            println("change activate: ${inputText}")
        }

        binding.buttonSave.setOnClickListener {
            println("I got inputEmail!: ${emailText.text}")
            println("I got input password!: ${passwordText.text}")


            when{
                emailText.text.toString().isNullOrEmpty() -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Email empty!")
                        .setMessage("Email address cannot be empty")
                        .setPositiveButton("OK", null)
                        .create()
                    alertDialog.show()
                }
                passwordText.text.toString().isNullOrEmpty() -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Password empty !")
                        .setMessage("Password cannot be empty")
                        .setPositiveButton("OK", null)
                        .create()
                    alertDialog.show()
                }
                passwordText.text.toString().length < 6 -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Password invalid")
                        .setMessage("Password length must not less than 6")
                        .setPositiveButton("OK", null)
                        .create()
                    alertDialog.show()
                }
                else->{
                    val callback = MyLoginCallback()
                    if(emailText.text.toString().matches(emailPattern.toRegex())){
                        vmMain.login(this.requireActivity().application, emailText.text.toString(), passwordText.text.toString(), callback)
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
    inner class MyLoginCallback : MainViewModel.LoginCallback {
        override fun onLoginSuccess(uid: String) {
            // 登录成功处理逻辑
            vmMain.setShowNav(true)
            Toast.makeText(this@MainFragment.requireContext(), "Login success!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_mainFragment2_to_profileFragment)

        }
        override fun onLoginFailure() {
            // 登录失败处理逻辑
            Toast.makeText(this@MainFragment.requireContext(), "Login failed. Check your password and email.", Toast.LENGTH_SHORT).show()
        }
    }
}
