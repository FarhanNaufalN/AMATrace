package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileProducerResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ProfileProducerData
)

data class ProfileProducerData(
    @SerializedName("id") val id: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("ownerName") val ownerName: String,
    @SerializedName("noHp") val noHp: String,
    @SerializedName("businessName") val businessName: String,
    @SerializedName("address") val address: String,
    @SerializedName("description") val description: String
)