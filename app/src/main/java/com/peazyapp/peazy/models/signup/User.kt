package com.peazyapp.peazy.models.signup

data class User(
    val _id: String,
    val card: Card,
    val createdAt: String,
    val device_token: String,
    val email: String,
    val is_deleted: Int,
    val is_verified: Boolean,
    val language_selected: Int,
    val name: String,
    val password: String,
    val privacy_policy: Boolean,
    val status: Int,
    val terms_and_condition: Boolean,
    val updatedAt: String
)