package com.example.lab3

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lab3.database.entity.SportDetail
import com.example.lab3.databinding.FragmentProfileDetailBinding
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
class SignUpFragment : BaseFragment(R.layout.fragment_sign_up) {
    private lateinit var binding: FragmentProfileDetailBinding


    private val vm : SignUpViewModel by activityViewModels()
    private val vmMain : MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val emailText = view.findViewById<EditText>(R.id.editTextEmail)
        val nameText = view.findViewById<EditText>(R.id.editTextName)
        val surnameText = view.findViewById<EditText>(R.id.editTextSurname)
        val telText = view.findViewById<EditText>(R.id.editTel)
        val passwordText = view.findViewById<EditText>(R.id.editTextPassword)
        val sButton = view.findViewById<Button>(R.id.buttonSave)
        val saveButton = view.findViewById<Button>(R.id.buttonRegister)
        super.onViewCreated(view, savedInstanceState)
        saveButton.setOnClickListener{
            vm.signUp(this.requireActivity().application,emailText.text.toString(), passwordText.text.toString())
        }
        sButton.setOnClickListener{
            vm.newUser(this.requireActivity().application,nameText.text.toString(),surnameText.text.toString(),telText.text.toString())
        }
    }

}
