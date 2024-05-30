package com.example.core.data.source.remote.response

data class GetProducerListResponse(
    val success: Boolean,
    val message: String,
    val data: ProducersData
)

data class ProducersData(
    val producers: List<Producers>,
)

data class Producers(
    val id: String,
    val businessName: String,
    val ownerName: String,
)