package com.example.amatrace.pages.supplier.ui.detail.tambahclaim

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amatrace.databinding.ActivityTambahClaimBinding
import com.example.amatrace.pages.adapter.ClaimAdapter
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.ClaimList

class TambahClaimActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahClaimBinding
    private lateinit var viewModel: TambahClaimViewModel
    private lateinit var adapter: ClaimAdapter
    private lateinit var myPreference: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahClaimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreference = Preference(this)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        val token = myPreference.getAccessToken()
        val productId = intent.getStringExtra("product_id")
        println("productId: $productId")

        // Set up RecyclerView
        adapter = ClaimAdapter()
        binding.rvClaims.adapter = adapter
        binding.rvClaims.layoutManager = LinearLayoutManager(this)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, TambahClaimViewModel.ViewModelFactory(this))
            .get(TambahClaimViewModel::class.java)

        // Observe data changes from ViewModel
        viewModel.claim.observe(this, { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        })

        if (token != null && productId != null) {
            viewModel.getAllClaim(token, productId)
        }
    }
}
