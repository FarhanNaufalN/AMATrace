package com.example.core.data.source.remote.response

data class UploadImageProfileSupplierResponse(
    val success: Boolean,
    val message: String,
    val data: ImageProfileSupplier

)
data class ImageProfileSupplier(
    val image: String
)