package com.peazyapp.peazy.models.menuitems

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName

data class Res(
    val count: Int,
    val image_base_path: String,

    @SerializedName("item")
    val item: JsonArray,
    val msg: String
)