package com.peazyapp.peazy.models.addpaycard

data class Err(
    val errCode: Int,
    val errs: Errs,
    val msg: String
)