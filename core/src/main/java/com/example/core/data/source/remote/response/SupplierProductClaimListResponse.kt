package com.example.core.data.source.remote.response

data class SupplierProductClaimListResponse (
    val success: Boolean,
    val message: String,
    val data: ClaimListData
)
data class ClaimListData(
    val claims: List<Claim>,
)

