package com.example.peazy.models.menuitems

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class Res(
    val count: Int,
    val image_base_path: String,
    @SerializedName("item")
    val item: JsonObject,
    val msg: String
)