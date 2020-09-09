package com.example.peazy.models.changepws

import com.google.gson.annotations.SerializedName

data class Err(
    @SerializedName("errCode")
    val errCode: Int,

    @SerializedName("msg")
    val msg: String
)