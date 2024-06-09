package com.example.core.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.core.data.source.paging.ProductProducerPagingSource
import com.example.core.data.source.paging.RawProductProducerPagingSource
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.response.Product
import com.example.core.data.source.remote.response.RawProduct
import kotlinx.coroutines.flow.Flow

class RawProductRepository(private val apiService: API, private val context: Context) {

    fun getProduct(searchQuery: String? = null ): LiveData<PagingData<RawProduct>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25
            ),
            pagingSourceFactory = {
                RawProductProducerPagingSource(apiService, context, searchQuery)
            }
        ).liveData
    }

    fun searchProducts(query: String): Flow<PagingData<RawProduct>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25
            ),
            pagingSourceFactory = {
                RawProductProducerPagingSource(apiService, context, query)
            }
        ).flow
    }
}