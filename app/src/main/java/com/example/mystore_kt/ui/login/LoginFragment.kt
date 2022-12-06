package com.example.mystore_kt.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.mystore_kt.R
import com.example.mystore_kt.databinding.FragmentLoginBinding
import com.example.mystore_kt.databinding.FragmentProductDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoginBinding.bind(view)

        binding.toolbar.apply {
            toolbarTitle.text = "Login"
            toolbarBack.setOnClickListener {
                activity?.onBackPressedDispatcher!!.onBackPressed()
            }
        }
        binding.loginBtn.setOnClickListener {

        }
    }
}