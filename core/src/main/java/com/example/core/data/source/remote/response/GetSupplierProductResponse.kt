package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProductListResponse(
    val success: Boolean,
    val message: String,
    val data: ProductData
)

data class ProductData(
    val products: List<Product>,
)

data class Product(
    val id: String,
    val image: String,
    val sku: String,
    val name: String
)


