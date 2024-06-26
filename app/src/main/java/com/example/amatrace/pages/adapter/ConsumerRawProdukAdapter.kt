package com.example.amatrace.pages.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.amatrace.R
import com.example.amatrace.pages.consumer.ui.detail.ConsumerSupplierDetailActivity
import com.example.core.data.source.remote.response.ConsumerRawProduct

class ConsumerRawProdukAdapter(
    var claimList: List<ConsumerRawProduct>
) : RecyclerView.Adapter<ConsumerRawProdukAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val claimTitle: TextView = itemView.findViewById(R.id.productName)
        val claimDescription: TextView = itemView.findViewById(R.id.productSku)
        val claimMass: TextView = itemView.findViewById(com.example.core.R.id.banyak_terpakai)
        val claimSupplier: TextView = itemView.findViewById(com.example.core.R.id.supplier_name)
        val claimImage: ImageView = itemView.findViewById(R.id.productImage)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val context = itemView.context
                val intent = Intent(context, ConsumerSupplierDetailActivity::class.java).apply {
                    putExtra("PRODUCT_ID", claimList[position].supplier.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.core.R.layout.item_rawproduk, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val claim = claimList[position]
        holder.claimTitle.text = claim.name
        holder.claimDescription.text = claim.sku
        holder.claimMass.text = claim.manyKgUsedinBatch.toString()
        holder.claimSupplier.text = claim.supplier.businessName

        Glide.with(holder.itemView.context).load(claim.image).into(holder.claimImage)
    }

    override fun getItemCount() = claimList.size
}
