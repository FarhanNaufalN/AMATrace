package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("role") val role: String,
    @SerializedName("id") val id: String,
    @SerializedName("ownerName") val ownerName: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("businessName") val businessName: String,
    @SerializedName("email") val email: String
)
