package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AddProductProducerResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: AddProductProducer
)

data class AddProductProducer(
    @SerializedName("id") val id: String,
    @SerializedName("sku") val sku: String,
    @SerializedName("name") val name: String,
)