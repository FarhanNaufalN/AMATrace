package com.example.amatrace.pages.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.amatrace.MainActivity
import com.example.core.data.source.remote.response.Shipping
import com.example.core.databinding.ItemRowBinding

class ShippingAdapter : PagingDataAdapter<Shipping, ShippingAdapter.MyViewHolder>(
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
        fun bind(item: Shipping) {
            binding.apply {
                productName.text = item.product.name
                productSku.text = item.serialNumberId
                Glide.with(itemView.context).load(item.product.image).into(productImage)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, MainActivity::class.java)
                    val bundle = Bundle()

                    bundle.putString("list_name", item.product.name)
                    bundle.putString("list_image", item.product.image)
                    bundle.putString("list_sku", item.product.sku)

                    intent.putExtras(bundle)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Shipping>() {
            override fun areItemsTheSame(oldItem: Shipping, newItem: Shipping): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Shipping, newItem: Shipping
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
