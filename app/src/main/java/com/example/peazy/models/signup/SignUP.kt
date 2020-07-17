package com.example.peazy.models.signup

import com.example.peazy.models.signup.Err
import com.google.gson.annotations.SerializedName

data class SignUP(
    @SerializedName("err")
    val err: Err,
    @SerializedName("status")
    val status: Int
)