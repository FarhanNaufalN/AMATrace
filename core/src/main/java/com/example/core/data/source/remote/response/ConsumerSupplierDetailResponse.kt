package com.example.core.data.source.remote.response

data class ConsumerSupplierDetailResponse(
    val success: Boolean,
    val message: String,
    val data: ConsumerSupplierData
)

data class ConsumerSupplierData(
    val id: String,
    val email: String,
    val ownerName: String,
    val businessName: String,
    val noHp: String,
    val address: String,
    val website: String?,
    val socialMediaLinks: List<String>,
    val heroImage: String,
    val galleryImages: List<String>
)
