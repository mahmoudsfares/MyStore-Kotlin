package com.example.mystore_kt.ui.signup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mystore_kt.R
import com.example.mystore_kt.databinding.FragmentLoginBinding
import com.example.mystore_kt.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment(R.layout.fragment_signup) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSignupBinding.bind(view)

        binding.toolbar.apply {
            toolbarTitle.text = "Signup"
            toolbarBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
        binding.signupBtn.setOnClickListener {

        }
    }
}