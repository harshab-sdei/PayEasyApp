package com.peazyapp.peazy.models.subsubcategory

data class Res(
    val count: Int,
    val image_base_path: String,
    val msg: String,
    val subcategory: List<Subcategory>
)