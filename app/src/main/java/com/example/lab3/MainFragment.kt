package com.example.lab3

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import com.example.lab3.databinding.FragmentMainBinding


class MainFragment : BaseFragment(R.layout.fragment_main), HasToolbar {
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

        val email = view.findViewById<EditText>(R.id.loginemail)
        val pw  = view.findViewById<EditText>(R.id.loginpw)
//        val inputEmail = binding.loginemail?.text.toString()
//        val inputPassword = binding.loginpw?.text.toString()
        binding.loginemail?.doOnTextChanged { inputText, _, _, _ ->
            // Respond to input text change
            println("I got inputTextl!: ${inputText}")
        }

        binding.btLogin.setOnClickListener {
            println("I got inputEmail!: ${email.text}")
            println("I got input password!: ${pw.text}")
        }

        binding.register.setOnClickListener {
            println("i click register|! need to jump to registration page!")
        }


    }
}