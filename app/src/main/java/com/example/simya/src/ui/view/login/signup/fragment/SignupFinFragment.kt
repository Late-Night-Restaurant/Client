package com.example.simya.src.ui.view.login.signup.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.simya.R
import com.example.simya.config.BaseFragment
import com.example.simya.src.ui.view.login.signup.SignupActivity
import com.example.simya.databinding.FragmentSignupFinBinding

class SignupFinFragment: BaseFragment<FragmentSignupFinBinding>(R.layout.fragment_signup_fin){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignupNext.setOnClickListener {
            requireActivity().finish()
        }
    }



}