package com.peazyapp.peazy.models.oderhistory

import com.peazyapp.peazy.models.verifypay.Err

data class OrderHistory(
    val err: Err,
    val res: Res,
    val status: Int
)