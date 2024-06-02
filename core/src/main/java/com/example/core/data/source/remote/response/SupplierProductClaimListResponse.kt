package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SupplierProductClaimListResponse (
    val success: Boolean,
    val message: String,
    val data: ClaimListData
)
data class ClaimListData(
    val claims: List<ClaimList>,
)
data class ClaimList(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("verificationStatus") val status: String,
    val product: Product
)