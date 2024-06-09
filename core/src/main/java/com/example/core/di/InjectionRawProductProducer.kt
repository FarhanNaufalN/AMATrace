package com.example.core.di

import android.content.Context
import com.example.core.data.repository.RawProductRepository
import com.example.core.data.source.remote.network.Config

object InjectionRawProductProducer {
    fun provideRepository(context: Context): RawProductRepository {
        val apiService = Config.getApiService()
        return RawProductRepository(apiService, context)
    }
}