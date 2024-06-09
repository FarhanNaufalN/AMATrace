package com.example.core.data.source.paging

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.Product
import retrofit2.HttpException
import java.io.IOException

class ProductProducerPagingSource(
    private val apiService: API,
    context: Context,
    private var searchQuery: String?
) : PagingSource<Int, Product>() {

    private var myPreferences = Preference(context)

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = myPreferences.getAccessToken()

            token?.let { accessToken ->
                val responseData = if (searchQuery.isNullOrEmpty()) {
                    apiService.getProducerProductList(accessToken, position, params.loadSize)
                } else {
                    apiService.getSearchProducerProductList(accessToken, searchQuery!!)
                }
                val products = responseData.data.products
                LoadResult.Page(
                    data = products,
                    prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                    nextKey = if (products.isEmpty()) null else position + 1
                )
            } ?: LoadResult.Error(NullPointerException("Access token is null"))
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


