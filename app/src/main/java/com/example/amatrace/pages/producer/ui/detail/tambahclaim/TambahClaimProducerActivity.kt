package com.example.amatrace.pages.producer.ui.detail.tambahclaim

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.amatrace.databinding.ActivityTambahClaimBinding
import com.example.amatrace.databinding.ActivityTambahClaimProducerBinding
import com.example.amatrace.pages.adapter.ClaimAdapter
import com.example.amatrace.pages.adapter.ClaimProductAdapter
import com.example.core.data.source.remote.preferences.Preference

class TambahClaimProducerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahClaimProducerBinding
    private lateinit var viewModel: TambahClaimProducerViewModel
    private lateinit var adapter: ClaimProductAdapter
    private lateinit var myPreference: Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahClaimProducerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreference = Preference(this)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        val token = myPreference.getAccessToken()
        val productId = intent.getStringExtra("product_id")
        println("productId: $productId")

        // Set up RecyclerView
        adapter = ClaimProductAdapter()
        binding.rvClaims.adapter = adapter
        binding.rvClaims.layoutManager = LinearLayoutManager(this)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this, TambahClaimProducerViewModel.ViewModelFactory(this))
            .get(TambahClaimProducerViewModel::class.java)

        // Observe data changes from ViewModel
        viewModel.claim.observe(this, { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        })

        if (token != null && productId != null) {
            viewModel.getAllClaim(token, productId)
        }
    }

    fun onBackButtonClicked(view: View) {
        onBackPressed()
    }
}
