package com.example.simya.src.model.account

import com.example.simya.config.BaseResponse

data class SignupResponse(
    var result: SignupResult?,
):BaseResponse(){
    fun getEmail(): String{
        return result!!.email
    }
    fun getProfile(): SignUpProfileDTO {
        return result!!.profile
    }
}
