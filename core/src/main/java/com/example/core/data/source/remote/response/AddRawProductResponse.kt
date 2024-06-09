package com.example.core.data.source.remote.response

data class AddRawProductResponse (
    val id: String,
    val product: Product,
    val supplier: Supplier,
    val productDetail: ProductDetailData,
)
