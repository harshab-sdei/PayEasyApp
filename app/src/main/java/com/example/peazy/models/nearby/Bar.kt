package com.example.peazy.models.nearby

data class Bar(
    val address: Address,
    val description: String,
    val distance: Double,
    val hours: String,
    val image: List<String>,
    val name: String,
    val p_commission: Int,
    val total_reviews: Int,
    val vat: Int
)