package com.example.amatrace.pages.supplier.ui.detail.tambahclaim

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amatrace.databinding.ActivityTambahClaimBinding
import com.example.amatrace.pages.adapter.ClaimProductSupplierAdapter
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.Claim
import com.example.core.data.source.remote.response.ProductListResponse
import com.example.core.data.source.remote.response.SupplierProductClaimListResponse

class TambahClaimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahClaimBinding
    private lateinit var adapter: ClaimProductSupplierAdapter
    private lateinit var myPreference: Preference


    private val viewModel: TambahClaimViewModel by viewModels {
        TambahClaimViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahClaimBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myPreference = Preference(this)

        val token = myPreference.getAccessToken()

        val bundle = intent.extras
        val productId = bundle?.getString("product_id")

        // Initialize RecyclerView
        adapter = ClaimProductSupplierAdapter()
        binding.rvClaim.apply {
            layoutManager = LinearLayoutManager(this@TambahClaimActivity)
            adapter = this@TambahClaimActivity.adapter
        }

        // Observe LiveData for claims
        viewModel.allClaim.observe(this, { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        })
        // Fetch claims
        productId?.let {
            if (token != null) {
                viewModel.getAllClaim(token ,it)
            }
        }
    }
}

