package com.example.peazy.models.menu_item

data class Res(
    val count: Int,
    val image_base_path: String,
    val item: List<Item>,
    val msg: String
)