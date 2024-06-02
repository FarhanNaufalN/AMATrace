package com.example.core.data.source.Paging

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.data.source.remote.network.API
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.ClaimList
import com.example.core.data.source.remote.response.Product
import retrofit2.HttpException
import java.io.IOException

class ClaimPagingSource (
    private val apiService: API,
    context: Context,
    private var productId: String?
) : PagingSource<Int, ClaimList>() {

    private val myPreferences = Preference(context)

    override fun getRefreshKey(state: PagingState<Int, ClaimList>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ClaimList> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = myPreferences.getAccessToken()
            val productId = this.productId
            if (token == null) {
                println("Access token is null")
            }else{
                println("Access token is not null")
            }
            if (productId == null) {
                println("id is null")
            }else{
                println("id is not null")
            }

            if (token != null && productId != null) {
                val responseData = apiService.getProductClaimSupplierList(token, productId, position, params.loadSize)
                val data = responseData.data.claims
                LoadResult.Page(
                    data = data,
                    prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                    nextKey = if (data.isEmpty()) null else position + 1
                )
            } else {
                LoadResult.Error(NullPointerException("Access token or product ID is null"))
            }
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
