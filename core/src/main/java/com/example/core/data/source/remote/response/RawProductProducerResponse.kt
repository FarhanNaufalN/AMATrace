package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseDataRawProduct(
    @SerializedName("id")
    val id: String,
    @SerializedName("expiredAt")
    val expiredAt: String,
    @SerializedName("remainingStock")
    val remainingStock: Int,
    @SerializedName("product")
    val product: ProductDetailData,
    @SerializedName("supplier")
    val supplier: Supplier,
    @SerializedName("shippingInfo")
    val shippingInfo: ShippingInfo
)


data class Supplier(
    @SerializedName("id")
    val id: String,
    @SerializedName("ownerName")
    val ownerName: String,
    @SerializedName("businessName")
    val businessName: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("noHp")
    val noHp: String
)

data class ShippingInfo(
    @SerializedName("deliveredBySupplierAt")
    val deliveredBySupplierAt: String,
    @SerializedName("note")
    val note: String,
    @SerializedName("mass")
    val mass: Int,
    @SerializedName("trackingHistory")
    val trackingHistory: TrackingHistoryRaw
)

data class TrackingHistoryRaw(
    @SerializedName("supplierLocation")
    val supplierLocation: LocationRaw,
    @SerializedName("receiptViaWarehouse")
    val receiptViaWarehouse: List<Receipt>,
    @SerializedName("arrival")
    val arrival: Arrival
)

data class LocationRaw(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("address")
    val address: String
)

data class Receipt(
    @SerializedName("receivedAt")
    val receivedAt: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double,
    @SerializedName("warehouse")
    val warehouse: Warehouse
)

data class Warehouse(
    @SerializedName("id")
    val id: String,
    @SerializedName("businessName")
    val businessName: String,
    @SerializedName("ownerName")
    val ownerName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("address")
    val address: String
)

data class Arrival(
    @SerializedName("arrivedAt")
    val arrivedAt: String,
    @SerializedName("lattitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("producer")
    val producer: Producer
)

data class Producer(
    @SerializedName("id")
    val id: String,
    @SerializedName("businessName")
    val businessName: String,
    @SerializedName("ownerName")
    val ownerName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("address")
    val address: String
)
