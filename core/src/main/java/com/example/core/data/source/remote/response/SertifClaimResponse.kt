package com.example.core.data.source.remote.response

data class SertifClaimResponse (
        val success: Boolean,
        val message: String,
        val data: SertifClaimData
)

data class SertifClaimData(
    val id: String,
    val product : Product,
    val productClaim: ProductClaim,
    val evidenceFile: String,
)

data class ProductClaim(
    val id: String,
    val title: String,
)


