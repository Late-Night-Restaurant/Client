package com.example.simya.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.simya.R
import com.example.simya.databinding.ActivitySigninEmailBinding
import java.util.regex.Pattern


class EmailLoginActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySigninEmailBinding
    private val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    // textWatcher 클린 코드 할 것
    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val emailInput = binding.tietEmailSigninInputEmail!!.text.toString()
            val passwordInput = binding.tietEmailSigninInputPassword!!.text.toString()
            if (emailInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                binding.btnEmailSigninLogin.isEnabled = true
                binding.btnEmailSigninLogin.setBackgroundResource(R.drawable.low_radius_button_on)
                binding.btnEmailSigninLogin.setTextColor(application.resources.getColor(R.color.Gray_03))
            }
            if (emailInput.isEmpty() || passwordInput.isEmpty()){
                binding.btnEmailSigninLogin.isEnabled = false
                binding.btnEmailSigninLogin.setBackgroundResource(R.drawable.low_radius_button_off)
                binding.btnEmailSigninLogin.setTextColor(application.resources.getColor(R.color.Gray_10))
            }
        }
        override fun afterTextChanged(s: Editable) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }
    private fun init(){
        binding.btnEmailSigninLogin.isEnabled = false

        //EditText 입력확인
        binding.tietEmailSigninInputEmail.addTextChangedListener(textWatcher)
        binding.tietEmailSigninInputPassword.addTextChangedListener(textWatcher)

        //로그인 이벤트
        binding.btnEmailSigninLogin.setOnClickListener {
            checkEmail()
            checkPassword()
        }

        binding.btnSigninEmailSignup.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

    }
    private fun checkEmail():Boolean{
        var email = binding.tietEmailSigninInputEmail.text.toString().trim() // 공백제거
        val pattern = Pattern.matches(emailValidation, email) // 패턴확인
        return if (pattern) {
            binding.tilEmailSigninInputEmail.error = null
            true
        } else {
            binding.tilEmailSigninInputEmail.error = "올바른 이메일 형식을 입력해주세요."
            false
        }
    }
    private fun checkPassword(): Boolean{
        var password = binding.tietEmailSigninInputPassword.text!!.length
        return if (password in 8..12) {
            binding.tilEmailSigninInputPassword.error = null
            true
        } else {
            binding.tilEmailSigninInputPassword.error = "영문과 숫자를 조합해서 입력해주세요.(8-12자)"
            false
        }
    }
    // 화면터치시 키보드 내려가게하는 이벤트
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }
}