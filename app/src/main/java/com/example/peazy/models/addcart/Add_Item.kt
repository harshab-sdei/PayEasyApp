package com.example.peazy.models.addcart

data class Add_Item(

    val image: String,
    val item_id: String,
    val name: String,
    val price: Double,
    var num_of_unit: Int
)