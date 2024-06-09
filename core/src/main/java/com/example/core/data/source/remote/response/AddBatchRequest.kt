package com.example.core.data.source.remote.response

data class ProductionResponse(
    val choosenProductId: String,
    val batchName: String,
    val expiredAt: String,
    val lotsOfProduction: Int,
    val rawProductsComposition: List<RawProductComposition>
)

data class RawProductComposition(
    val rawProductId: String,
    val manyUsed: Int
)