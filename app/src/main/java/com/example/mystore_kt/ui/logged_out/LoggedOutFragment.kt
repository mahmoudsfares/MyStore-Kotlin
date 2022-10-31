package com.example.mystore_kt.ui.logged_out

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mystore_kt.R
import com.example.mystore_kt.databinding.FragmentLoggedOutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoggedOutFragment : Fragment(R.layout.fragment_logged_out) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoggedOutBinding.bind(view)

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(LoggedOutFragmentDirections.actionLoggedOutFragmentToLoginFragment())
        }
        binding.signupBtn.setOnClickListener {
            findNavController().navigate(LoggedOutFragmentDirections.actionLoggedOutFragmentToSignupFragment())
        }
    }
}