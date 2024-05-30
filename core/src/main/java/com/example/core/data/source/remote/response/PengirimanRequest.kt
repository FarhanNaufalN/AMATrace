package com.example.core.data.source.remote.response

data class ShippingRequest(
    val productId: String,
    val deliveryDate: String,
    val expiredDate: String,
    val mass: Int,
    val producerIdDestination: String,
    val notes: String,
    val serialNumber: String
)