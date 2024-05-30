package com.example.core.di

import android.content.Context
import com.example.core.data.repository.SupplierProductRepository
import com.example.core.data.source.remote.network.Config

object InjectionProductSupplier {
    fun provideRepository(context: Context): SupplierProductRepository {
        val apiService = Config.getApiService()
        return SupplierProductRepository(apiService, context)
    }
}