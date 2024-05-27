package com.example.core.data.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: LoginData?
)

data class LoginData(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("account") val account: Account
)
