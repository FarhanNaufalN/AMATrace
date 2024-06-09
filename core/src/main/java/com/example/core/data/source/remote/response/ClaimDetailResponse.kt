package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SupplierDetailClaimResponse(
    val success: Boolean,
    val message: String,
    val data: ClaimData
)

data class ClaimData(
    val claim: Claim,
    val product: Product,
    val verification: Verification
)

data class Verification(
    val status: String,
    @SerializedName("expiredAt") val expiredAt: String?,
    @SerializedName("requestedAt") val requestedAt: String?,
    @SerializedName("evidenceFile") val evidenceFile: String?
)
