package com.example.peazy.models.subsubcategory


data class SubItem(
    val description: String,
    val image: String,
    val item_id: String,
    val name: String,
    val price: Int,
    val sub_sub_cat_id: String,
    var num_of_unit: Int
)