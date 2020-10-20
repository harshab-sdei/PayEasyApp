package com.peazyapp.peazy.models.signup

data class Res(
    val access_token: String,
    val is_verified: Boolean,
    val user: User
)