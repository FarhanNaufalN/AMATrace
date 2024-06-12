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
import com.example.amatrace.pages.producer.ui.detail.rawProduk.DetailRawProdukActivity
import com.example.amatrace.pages.supplier.ui.detail.product.DetailProductActivity
import com.example.core.data.source.remote.response.Product
import com.example.core.data.source.remote.response.RawProduct
import com.example.core.databinding.ItemRowBinding

class RawProductProducerAdapter : PagingDataAdapter<RawProduct, RawProductProducerAdapter.MyViewHolder>(
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
        fun bind(item: RawProduct) {

            binding.apply {
                productName.text = item.product.name
                productSku.text = item.supplier.businessName
                Glide.with(itemView.context).load(item.product.image).into(productImage)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailRawProdukActivity::class.java)
                    val bundle = Bundle()

                    bundle.putString("product_id", item.id)
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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RawProduct>() {
            override fun areItemsTheSame(oldItem: RawProduct, newItem: RawProduct): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: RawProduct, newItem: RawProduct
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}