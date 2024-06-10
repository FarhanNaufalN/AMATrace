package com.example.core.di

import android.content.Context
import com.example.core.data.repository.BatchProductRepository
import com.example.core.data.repository.RawProductRepository
import com.example.core.data.source.remote.network.Config

object InjectionBatchProductProducer {
    fun provideRepository(context: Context): BatchProductRepository {
        val apiService = Config.getApiService()
        return BatchProductRepository(apiService, context)
    }
}