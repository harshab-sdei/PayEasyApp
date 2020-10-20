package com.peazyapp.peazy.models.verifypay

data class StripeToken(
    val _id: String,
    val amount: Int,
    val bar_id: String,
    val createdAt: String,
    val instruction: String,
    val item: List<Item>,
    val mode: Int,
    val order_id: String,
    val order_status: Int,
    val status: Int,
    val stripe_token: String,
    val tips: Int,
    val updatedAt: String,
    val user_id: String
)