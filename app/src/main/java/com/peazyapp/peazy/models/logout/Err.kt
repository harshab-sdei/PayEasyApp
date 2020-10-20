package com.peazyapp.peazy.models.logout

data class Err(
    val errCode: Int,
    val errs: Errs,
    val msg: String
)