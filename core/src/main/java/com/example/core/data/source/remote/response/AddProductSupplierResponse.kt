package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class AddProductSupplierResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: AddProductSupplier
)

data class AddProductSupplier(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
)