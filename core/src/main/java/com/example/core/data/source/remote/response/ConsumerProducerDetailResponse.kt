package com.example.core.data.source.remote.response

data class ConsumerProducerDetailResponse(
    val success: Boolean,
    val message: String,
    val data: ConsumerProducerData
)

data class ConsumerProducerData(
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
