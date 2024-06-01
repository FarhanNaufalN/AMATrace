package com.example.core.data.source.remote.response

data class SearchProductResponse (
    val success: Boolean,
    val message: String,
    val data: ProductData
)
