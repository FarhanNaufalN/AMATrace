package com.example.amatrace.pages.supplier.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.amatrace.R
import com.example.amatrace.adapter.ProductPagingAdapter
import com.example.core.data.remote.network.API
import com.example.core.data.remote.network.Config
import com.example.core.data.remote.preferences.Preference
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var productAdapter: ProductPagingAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ProductViewModel

    private lateinit var accessToken: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_supplier, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_supplier)
        productAdapter = ProductPagingAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = productAdapter

        accessToken = Preference(requireContext()).getAccessToken() ?: ""

        if (accessToken.isEmpty()) {
            // Handle the case where the access token is not available
            return
        }

        val api = Config.getApiService()

        viewModel = ViewModelProvider(this, ViewModelFactory(api, accessToken))
            .get(ProductViewModel::class.java)

        lifecycleScope.launch {
            viewModel.productPagingData.collectLatest { pagingData ->
                productAdapter.submitData(pagingData)
            }
        }
    }
}

class ViewModelFactory(
    private val api: API,
    private val token: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            return ProductViewModel(api, token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
