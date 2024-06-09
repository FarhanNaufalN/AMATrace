package com.example.core.data.source.remote.response

data class BatchResponse (
    val success: Boolean,
    val message: String,
    val data: BatchData
)

data class BatchData(
    val id: String,
    val qrCode: String,
    val batchName: String,

)