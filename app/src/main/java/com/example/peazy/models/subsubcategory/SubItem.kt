package com.example.peazy.models.subsubcategory


data class SubItem(
    val description: String,
    val image: List<String>,
    val item_id: String,
    val name: String,
    val price: Double,
    val sub_sub_cat: String,
    val sub_sub_cat_id: String,
    var num_of_unit: Int
)