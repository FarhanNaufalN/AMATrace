package com.example.core.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.core.data.source.Paging.ClaimPagingSource
import com.example.core.data.source.Paging.ClaimProducerPagingSource
import com.example.core.data.source.Paging.ShippingListPagingSource
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.response.ClaimList


class ClaimProducerRepository (private val apiService: API, private val context: Context) {

    fun getClaim(productId: String): LiveData<PagingData<ClaimList>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ClaimProducerPagingSource(apiService, context, productId)
            }
        ).liveData
    }
}