package com.example.peazy.models.editprofile

import com.example.peazy.models.booktable.Err

data class EditProfile(
    val err: Err,
    val res: Res,
    val status: Int
)