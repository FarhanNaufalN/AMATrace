package com.example.core.data.source.remote.response

data class ListBatchResponse(
    val success: Boolean,
    val message: String,
    val data: DataBatch
)

data class DataBatch(
    val productBatches: List<ProductBatch>,
    val totalData: Int,
    val totalPage: Int
)

data class ProductBatch(
    val id: String,
    val batchName: String,
    val product: Product
)

