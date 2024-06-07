package com.example.core.data.source.remote.response

data class SupplierShippingDetailProducerResponse(
    val success: Boolean,
    val message: String,
    val data: SupplierShippingDetailData
)

data class SupplierShippingDetailData(
    val id: String,
    val qrCode: String,
    val serialNumber: String,
    val createdAt: String,
    val deliveryDate: String,
    val expiredDate: String,
    val mass: Int,
    val note: String,
    val supplier: Supplier,
    val product: ProductDetailData,
    val producerDestination: ProducerDestination,
    val trackingHistory: TrackingHistory
)


data class ReceiptViaWarehouse(
    val receivedAt: String,
    val lat: Double,
    val lon: Double,
    val warehouse: Warehouse
)

