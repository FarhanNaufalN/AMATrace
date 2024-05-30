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
import com.example.core.data.source.remote.response.Product
import com.example.core.databinding.ItemRowBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ProductSupplierAdapter : PagingDataAdapter<Product, ProductSupplierAdapter.MyViewHolder>(
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
        fun bind(item: Product) {

            binding.apply {
                productName.text = item.name
                productSku.text = item.sku
                Glide.with(itemView.context).load(item.image).into(productImage)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, MainActivity::class.java)
                    val bundle = Bundle()

                    bundle.putString("list_name", item.name)
                    bundle.putString("list_image", item.image)
                    bundle.putString("list_sku", item.sku)

                    intent.putExtras(bundle)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Product, newItem: Product
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}