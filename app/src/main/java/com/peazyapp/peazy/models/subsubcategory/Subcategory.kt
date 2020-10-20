package com.peazyapp.peazy.models.subsubcategory

data class Subcategory(
    val name: String,
    val subcat_id: String,
    var subItem: List<SubItem>
)