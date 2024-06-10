package com.example.core.data.source.paging

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.Product
import com.example.core.data.source.remote.response.ProductBatch
import com.example.core.data.source.remote.response.RawProduct
import retrofit2.HttpException
import java.io.IOException

class BatchProductProducerPagingSource(
    private val apiService: API,
    private val context: Context,
    private var searchQuery: String?
) : PagingSource<Int, ProductBatch>() {

    private val myPreferences = Preference(context)

    override fun getRefreshKey(state: PagingState<Int, ProductBatch>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductBatch> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = myPreferences.getAccessToken() ?: return LoadResult.Error(NullPointerException("Access token is null"))

            val responseData = if (searchQuery.isNullOrEmpty()) {
                apiService.getProducerBatchProductList(token, position, params.loadSize)
            } else {
                apiService.getSearchBatchProducerProductList(token, searchQuery!!)
            }

            val products = responseData.data.productBatches

            LoadResult.Page(
                data = products,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (products.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    fun setSearchQuery(query: String?) {
        searchQuery = query
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
