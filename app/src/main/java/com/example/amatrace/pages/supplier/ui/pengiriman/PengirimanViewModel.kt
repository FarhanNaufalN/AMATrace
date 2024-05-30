package com.example.amatrace.pages.supplier.ui.pengiriman

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.amatrace.pages.supplier.ui.home.HomeViewModel
import com.example.amatrace.pages.supplier.ui.pengiriman.ShippingViewModelFactory.Companion.TAG
import com.example.core.data.repository.ShippingListRepository
import com.example.core.data.source.remote.network.Config
import com.example.core.data.source.remote.response.GetShippingListResponse
import com.example.core.data.source.remote.response.ProductListResponse
import com.example.core.data.source.remote.response.Shipping
import com.example.core.di.InjectionProductSupplier
import com.example.core.di.InjectionShippingSupplier
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengirimanViewModel (shippingListRepository: ShippingListRepository) : ViewModel() {
    private val _listShipping = MutableLiveData<List<Shipping>>()
    private val listShipping: LiveData<List<Shipping>> = _listShipping

    val shipping: LiveData<PagingData<Shipping>> by lazy {
        shippingListRepository.getShippingList().cachedIn(viewModelScope)
    }

    fun getShippingPaging() = listShipping

    fun getAllShipping(token: String){
        val client = Config.getApiService().getListShipping(token)
        client.enqueue(object : Callback<GetShippingListResponse> {
            override fun onResponse(
                call: Call<GetShippingListResponse>,
                response: Response<GetShippingListResponse>
            ) {
                if (response.isSuccessful) {
                    _listShipping.postValue(response.body()?.data?.shippings)
                }
            }

            override fun onFailure(call: Call<GetShippingListResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }


}

class ShippingViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PengirimanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PengirimanViewModel(InjectionShippingSupplier.provideShippingRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        const val TAG = "ShippingViewModelFactory"
    }
}