package com.example.peazy.models.nearby

import com.example.peazy.models.signup.Err

data class NearByBar(
    val err: Err,
    val res: Res,
    val status: Int
)