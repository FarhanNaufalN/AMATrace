package com.example.amatrace.pages.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.amatrace.pages.producer.ui.detail.productproducer.DetailProdukProducerActivity
import com.example.amatrace.pages.supplier.ui.detail.product.DetailProductActivity
import com.example.core.data.source.remote.response.Product
import com.example.core.data.source.remote.response.ProductBatch
import com.example.core.data.source.remote.response.RawProduct
import com.example.core.databinding.ItemRowBinding

class BatchProductProducerAdapter : PagingDataAdapter<ProductBatch, BatchProductProducerAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    class MyViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductBatch) {

            binding.apply {
                productName.text = item.product.name
                productSku.text = item.product.sku
                Glide.with(itemView.context).load(item.product.image).into(productImage)

            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductBatch>() {
            override fun areItemsTheSame(oldItem: ProductBatch, newItem: ProductBatch): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ProductBatch, newItem: ProductBatch
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}