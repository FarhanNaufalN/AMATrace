package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SupplierProductShippingDetailResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ShippingDetail
)

data class ShippingDetail(
    @SerializedName("id") val id: String,
    @SerializedName("qrCode") val qrCode: String,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("deliveryDate") val deliveryDate: String,
    @SerializedName("expiredDate") val expiredDate: String,
    @SerializedName("mass") val mass: Int,
    @SerializedName("note") val note: String,
    @SerializedName("product") val product: Product,
    @SerializedName("producerDestination") val producerDestination: ProducerDestination,
    @SerializedName("trackingHistory") val trackingHistory: TrackingHistory
)

data class ProducerDestination(
    @SerializedName("id") val id: String,
    @SerializedName("businessName") val businessName: String,
    @SerializedName("ownerName") val ownerName: String,
    @SerializedName("email") val email: String,
    @SerializedName("address") val address: String
)

data class TrackingHistory(
    @SerializedName("supplierLocation") val supplierLocation: Location,
    @SerializedName("receiptViaWarehouse") val receiptViaWarehouse: List<Any>, // Ubah tipe data sesuai kebutuhan Anda
    @SerializedName("arrival") val arrival: Arrival
)

data class Location(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("address") val address: String
)
