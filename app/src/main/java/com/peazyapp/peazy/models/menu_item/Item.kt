package com.peazyapp.peazy.models.menu_item

data class Item(
    val description: String,
    val image: List<String>,
    val item_id: String,
    val name: String,
    val price: Double,
    val sub_sub_cat: String,
    val sub_sub_cat_id: String
)