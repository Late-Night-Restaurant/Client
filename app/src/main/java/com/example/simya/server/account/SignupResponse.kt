package com.example.simya.server.account

data class SignupResponse(
    var isSuccess: Boolean?,
    var code: Int?,
    var message: String?,
    var result: SignupResult?,
){
    fun getEmail(): String{
        return result!!.email
    }
    fun getProfile(): ProfileDTO {
        return result!!.profile
    }
}
