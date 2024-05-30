package com.example.core.data.source.remote.response

data class ShippingResponse(
    val success: Boolean,
    val message: String,
    val data: ShippingData
)

data class ShippingData(
    val id: String,
    val serialNumberId: String,
    val qrCode: String
)