package com.example.peazy.models.signup

import com.google.gson.annotations.SerializedName

data class SignUP(
    @SerializedName("err")
    val err: Err,
    @SerializedName("status")
    val status: Int,
    val res: Res

)