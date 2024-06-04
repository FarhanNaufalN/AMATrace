package com.example.amatrace.pages.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.amatrace.R
import com.example.core.data.source.remote.response.Claim
import com.example.core.databinding.ItemRowBinding

class ClaimDetailAdapter(var claimList: List<Claim>) : RecyclerView.Adapter<ClaimDetailAdapter.ClaimViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClaimViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRowBinding.inflate(inflater, parent, false)
        return ClaimViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ClaimViewHolder, position: Int) {
        val claim = claimList[position]
        holder.bind(claim)
    }

    override fun getItemCount(): Int {
        return claimList.size
    }

    inner class ClaimViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.productName)
        private val productSkuTextView: TextView = itemView.findViewById(R.id.productSku)
        private val productImage: ImageView = itemView.findViewById(R.id.productImage)

        fun bind(claim: Claim) {
            productNameTextView.text = claim.name
            productSkuTextView.text = claim.status
            Glide.with(itemView.context).load(claim.icon).into(productImage)
        }
    }
}

