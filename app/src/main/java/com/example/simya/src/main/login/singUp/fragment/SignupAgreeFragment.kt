package com.example.simya.src.main.login.singUp.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.simya.R
import com.example.simya.src.main.login.signIn.EmailLoginActivity
import com.example.simya.src.main.login.singUp.SignupActivity
import com.example.simya.databinding.FragmentSignupAgreeBinding
import com.example.simya.databinding.SnackbarLayoutBinding
import com.google.android.material.snackbar.Snackbar


class SignupAgreeFragment: Fragment(), SignupActivity.onBackPressedListener {
    private lateinit var binding: FragmentSignupAgreeBinding

    var signupActivity: SignupActivity? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        signupActivity = context as SignupActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupAgreeBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        agreeCheck()
        binding.btnSignupNextAgree.setOnClickListener {

            if (agreeCheck()) {
                signupActivity!!.nextFragmentSignUp(2)
                initAgree()
            } else {
                onSnackBar(binding.root, "동의하지 않았습니다.")
            }
        }

    }


    private fun agreeCheck() : Boolean {
        val agreeAll = binding.cbSignupAgreeAll
        val agreeService = binding.cbSignupAgreeService
        val agreeInfo = binding.cbSignupAgreeInfo

        if (agreeAll.isChecked) {
            agreeService.isChecked = true
            agreeInfo.isChecked = true
            TrueButton()
        } else {
            agreeService.isChecked = false
            agreeInfo.isChecked = false
            FalseButton()
        }

        agreeAll.setOnClickListener {
            if (agreeAll.isChecked) {
                agreeService.isChecked = true
                agreeInfo.isChecked = true
                TrueButton()
            } else {
                agreeService.isChecked = false
                agreeInfo.isChecked = false
                FalseButton()
            }
        }

        agreeInfo.setOnClickListener {
            if (agreeInfo.isChecked) {
                if (agreeService.isChecked) {
                    agreeAll.isChecked = true
                    TrueButton()
                } else {
                    agreeAll.isChecked = false
                    FalseButton()
                }
            }
            if (!agreeInfo.isChecked) {
                agreeAll.isChecked = false
                FalseButton()
            }
        }

        agreeService.setOnClickListener {
            if (agreeService.isChecked) {
                if (agreeInfo.isChecked) {
                    agreeAll.isChecked = true
                    TrueButton()
                } else {
                    agreeAll.isChecked = false
                    FalseButton()
                }
            }
            if (!agreeService.isChecked) {
                agreeAll.isChecked = false
                FalseButton()
            }
        }



        return agreeAll.isChecked

    }

    private fun TrueButton() {
        binding.btnSignupNextAgree.isEnabled = true
        binding.btnSignupNextAgree.isClickable = true
        binding.btnSignupNextAgree.setBackgroundResource(R.drawable.low_radius_button_on)
        binding.btnSignupNextAgree.setTextColor(resources.getColor(R.color.Gray_03))

    }

    private fun FalseButton() {
        binding.btnSignupNextAgree.isEnabled = false
        binding.btnSignupNextAgree.isClickable = false
        binding.btnSignupNextAgree.setBackgroundResource(R.drawable.low_radius_button_off)
        binding.btnSignupNextAgree.setTextColor(resources.getColor(R.color.Gray_10))

    }

    private fun initAgree() {
        val agreeAll = binding.cbSignupAgreeAll
        val agreeService = binding.cbSignupAgreeService
        val agreeInfo = binding.cbSignupAgreeInfo

        agreeAll.isChecked = false
        agreeService.isChecked = false
        agreeInfo.isChecked = false
    }

    override fun onBackPressed() {
        val intent = Intent(this.context, EmailLoginActivity::class.java)
        startActivity(intent)
    }


    // SnackBar 구현
    private fun onSnackBar(view: View, message: String){
        var snackBar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)

        val snackBarView: View = layoutInflater.inflate(R.layout.snackbar_layout, null)
        val snackBarBinding = SnackbarLayoutBinding.bind(snackBarView)
        snackBar.view.setBackgroundColor(Color.TRANSPARENT)
        snackBarBinding.snackBarMessage.text = message

        val snackBarLayout = snackBar.view as Snackbar.SnackbarLayout
        snackBarLayout.addView(snackBarView)

        snackBar.show()
    }
}