package com.example.peazy.models.nearbyplace

data class NearByPlace(
    val html_attributions: List<Any>,
    val results: List<Result>,
    val status: String
)