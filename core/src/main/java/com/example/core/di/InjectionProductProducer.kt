package com.example.core.di

import android.content.Context
import com.example.core.data.repository.ProducerProductRepository
import com.example.core.data.repository.SupplierProductRepository
import com.example.core.data.source.remote.network.Config

object InjectionProductProducer {
    fun provideRepository(context: Context): ProducerProductRepository {
        val apiService = Config.getApiService()
        return ProducerProductRepository(apiService, context)
    }
}