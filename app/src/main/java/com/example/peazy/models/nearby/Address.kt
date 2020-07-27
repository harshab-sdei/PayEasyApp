package com.example.peazy.models.nearby

data class Address(
    val city: String,
    val country: String,
    val latlong: Latlong,
    val street: String,
    val zip_code: String
)