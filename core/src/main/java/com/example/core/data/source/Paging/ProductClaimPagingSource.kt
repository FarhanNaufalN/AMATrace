package com.example.core.data.source.Paging

import android.content.Context
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.Claim
import com.example.core.data.source.remote.response.Product
import retrofit2.HttpException
import java.io.IOException

class ProductClaimPagingSource (private val apiService: API, context: Context) :
    PagingSource<Int, Claim>() {

    private var myPreferences = Preference(context)
    private var productId = String

    override fun getRefreshKey(state: PagingState<Int, Claim>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Claim> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = myPreferences.getAccessToken()
            val productId = productId

            token?.let { accessToken ->
                val responseData = apiService.getProductClaimSupplierList(accessToken,
                    productId.toString(), position, params.loadSize)
                val data = responseData.data.claims
                LoadResult.Page(
                    data = data,
                    prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                    nextKey = if (data.isEmpty()) null else position + 1
                )
            } ?: LoadResult.Error(NullPointerException("Access token is null"))
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1

        fun snapshot(items: List<Claim>): PagingData<Claim> {
            return PagingData.from(items)
        }
    }
}