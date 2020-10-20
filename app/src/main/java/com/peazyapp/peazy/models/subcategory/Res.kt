package com.peazyapp.peazy.models.subcategory

data class Res(
    val count: Int,
    val image_base_path: String,
    val msg: String,
    val subcategory: List<SubcategoryX>
)