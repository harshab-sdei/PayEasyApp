package com.peazyapp.peazy.models.viewcard

import com.google.gson.annotations.SerializedName

data class Err(
    @SerializedName("errCode")
    val errCode: Int,

    @SerializedName("msg")
    val msg: String
)