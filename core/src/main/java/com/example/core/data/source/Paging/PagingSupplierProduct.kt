package com.example.core.data.source.Paging

import android.content.Context
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.Product
import retrofit2.HttpException
import java.io.IOException

class ProductPagingSource(
    private val api: API,
    private val token: String
) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 1
        return try {
            val response = api.getProductList(token, page, params.loadSize).execute()
            if (response.isSuccessful && response.body()?.success == true) {
                val data = response.body()?.data?.products ?: emptyList()
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception("Failed to load data"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
