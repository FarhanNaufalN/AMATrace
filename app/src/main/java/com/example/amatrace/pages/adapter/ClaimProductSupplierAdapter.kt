package com.example.amatrace.pages.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core.data.source.remote.response.Claim
import com.example.core.databinding.ItemClaimBinding

class ClaimProductSupplierAdapter(private val claims: List<Claim>) :
    RecyclerView.Adapter<ClaimProductSupplierAdapter.ClaimViewHolder>() {

    inner class ClaimViewHolder(private val binding: ItemClaimBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(claim: Claim) {
            binding.claimName.text = claim.name
            Glide.with(binding.root)
                .load(claim.icon)
                .into(binding.claimIcon)
            binding.claimStatus.text = claim.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimViewHolder {
        val binding = ItemClaimBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClaimViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClaimViewHolder, position: Int) {
        holder.bind(claims[position])
    }

    override fun getItemCount(): Int = claims.size
}
