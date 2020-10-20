package com.peazyapp.peazy.models.signup

import com.google.gson.annotations.SerializedName

data class Msg(
    @SerializedName("fieldName")
    val fieldName: String,

    @SerializedName("message")
    val message: String
)