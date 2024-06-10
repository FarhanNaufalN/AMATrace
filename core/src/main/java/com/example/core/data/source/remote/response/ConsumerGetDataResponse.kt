package com.example.core.data.source.remote.response


data class ConsumerGetDataResponse(
    val success: Boolean,
    val message: String,
    val data: ConsumerData
)

data class ConsumerData(
    val product: ConsumerProduct,
    val claims: List<ConsumerClaim>,
    val producer: ConsumerProducer,
    val productionBatch: ConsumerProductionBatch,
    val rawProducts: List<ConsumerRawProduct>
)


data class ConsumerProduct(
    val id: String,
    val name: String,
    val sku: String,
    val image: String,
    val description: String,
    val ingredients: String
)

data class ConsumerClaim(
    val id: String,
    val title: String,
    val icon: String,
    val status: String,
    val expiredAt: String?
)

data class ConsumerProducer(
    val id: String,
    val ownerName: String,
    val businessName: String,
    val avatar: String
)

data class ConsumerProductionBatch(
    val batchName: String,
    val createdAt: String,
    val expiredAt: String,
    val lotsOfProduction: Int
)

data class ConsumerRawProduct(
    val id: String,
    val name: String,
    val sku: String,
    val image: String,
    val description: String,
    val manyKgUsedinBatch: Int,
    val supplier: ConsumerSupplier,
    val shipping: List<ConsumerShipping>
)

data class ConsumerSupplier(
    val id: String,
    val businessName: String,
    val ownerName: String,
    val address: String,
    val email: String,
    val avatar: String
)

data class ConsumerShipping(
    val id: String,
    val serialNumber: String,
    val deliveredBySupplierAt: String,
    val receivedByProducerAt: String,
    val expireDate: String,
    val mass: Int,
    val manyKgUsedInProductionBatch: Int
)

