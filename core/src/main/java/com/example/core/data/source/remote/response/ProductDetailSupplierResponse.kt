package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProductDetailSupplierResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ProductDetailData
)

data class ProductDetailData(
    @SerializedName("id") val id: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("name") val name: String,
    @SerializedName("sku") val sku: String,
    @SerializedName("image") val image: String,
    @SerializedName("description") val description: String,
    @SerializedName("ingredients") val ingredients: String,
    @SerializedName("claims") val claims: List<Claim>
)

data class Claim(
     val id: String,
   val name: String,
     val icon: String,
    val status: String,
    val description: String,
     val title: String,
    val evidences: List<String>
)