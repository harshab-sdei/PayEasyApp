package com.example.peazy.models.addpaycard

data class Err(
    val errCode: Int,
    val errs: Errs,
    val msg: String
)