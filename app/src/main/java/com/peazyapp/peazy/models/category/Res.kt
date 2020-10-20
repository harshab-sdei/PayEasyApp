package com.peazyapp.peazy.models.category

data class Res(
    val category: List<Category>,
    val count: Int,
    val image_base_path: String,
    val msg: String
)