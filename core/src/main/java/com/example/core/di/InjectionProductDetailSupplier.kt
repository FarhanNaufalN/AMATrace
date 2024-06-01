package com.example.core.di

import android.content.Context
import com.example.core.data.repository.ProductDetailRepository
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.network.Config

object InjectionProductDetailSupplier {
    fun provideRepository(context: Context): ProductDetailRepository {
        val apiService = Config.getApiService()
        return ProductDetailRepository(apiService)
    }

}