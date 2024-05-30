package com.example.core.data.source.remote.response

data class GetShippingListResponse(
    val success: Boolean,
    val message: String,
    val data: ShippingsData
)

data class ShippingsData(
    val shippings: List<Shipping>,
)

data class Shipping(
    val id: String,
    val serialNumberId: String,
    val product: ProductShipping
)

data class ProductShipping(
    val id: String,
    val name: String,
    val sku: String,
    val image: String
)