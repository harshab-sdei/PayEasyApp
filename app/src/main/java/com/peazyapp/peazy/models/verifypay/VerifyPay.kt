package com.peazyapp.peazy.models.verifypay

data class VerifyPay(
    val err: Err,
    val res: Res,
    val status: Int
)