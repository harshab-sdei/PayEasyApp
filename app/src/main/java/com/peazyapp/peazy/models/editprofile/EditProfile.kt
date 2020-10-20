package com.peazyapp.peazy.models.editprofile

import com.peazyapp.peazy.models.booktable.Err

data class EditProfile(
    val err: Err,
    val res: Res,
    val status: Int
)