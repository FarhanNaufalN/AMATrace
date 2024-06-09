package com.example.core.data.source.remote.response

data class getRawProductResponse (
    val success: Boolean,
    val message: String,
    val data: List<RawProduct>
)
