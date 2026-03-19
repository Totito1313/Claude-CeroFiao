package com.schwarckdev.cerofiao.core.model

data class CategoryNode(
    val category: Category,
    val subcategories: List<Category>
)
