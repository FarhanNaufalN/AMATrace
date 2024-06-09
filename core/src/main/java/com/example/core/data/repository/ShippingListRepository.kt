package com.example.core.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.core.data.source.paging.ShippingListPagingSource
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.response.Shipping
import kotlinx.coroutines.flow.Flow

class ShippingListRepository (private val apiService: API, private val context: Context) {

    fun getShippingList(searchQuery: String? = null ): LiveData<PagingData<Shipping>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25
            ),
            pagingSourceFactory = {
                ShippingListPagingSource(apiService, context, searchQuery)
            }
        ).liveData
    }

    fun searchShipping(query: String): Flow<PagingData<Shipping>> {
        return Pager(
            config = PagingConfig(
                pageSize = 25
            ),
            pagingSourceFactory = {
                ShippingListPagingSource(apiService, context, query)
            }
        ).flow
    }
}