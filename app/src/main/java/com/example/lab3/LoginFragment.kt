package com.example.lab3

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val vmMain : MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emailText = view.findViewById<EditText>(R.id.editTextEmail)
        val passwordText = view.findViewById<EditText>(R.id.editTextPassword)
        val sButton = view.findViewById<Button>(R.id.buttonSave)
        val rButton = view.findViewById<Button>(R.id.buttonRegister)

        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        sButton.setOnClickListener{
            when{
                emailText.text.isNullOrEmpty() -> {
                    val alertDialog = AlertDialog.Builder(context)
                        .setTitle("Email empty!")
                        .setMessage("Email address cannot be empty")
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
                        if(!vmMain.UID.isNullOrEmpty()){
                            Toast.makeText(this.requireContext(), "Login success!.", Toast.LENGTH_SHORT).show()
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
        rButton.setOnClickListener{
            //findNavController().navigate(R.id.to_SignUpFragment)
            //findNavController().navigate(R.id.action_courtFragment_to_courtDetailFragment)
        }
    }



}
