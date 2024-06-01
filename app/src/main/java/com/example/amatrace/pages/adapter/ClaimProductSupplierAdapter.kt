package com.example.amatrace.pages.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.amatrace.pages.supplier.ui.detail.product.DetailProductActivity
import com.example.core.data.source.remote.response.Claim
import com.example.core.data.source.remote.response.Product
import com.example.core.databinding.ItemRowBinding

class ClaimProductSupplierAdapter: PagingDataAdapter<Claim, ClaimProductSupplierAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    private var claims: List<Claim> = emptyList() // Menyimpan daftar klaim

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
        fun bind(item: Claim?) {

            binding.apply {
                if (item != null) {
                    productName.text = item.id
                    productSku.text = item.name
                    Glide.with(itemView.context).load(item.icon).into(productImage)
                }

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailProductActivity::class.java)
                    val bundle = Bundle()

                    if (item != null) {
                        bundle.putString("product_id", item.id)
                        bundle.putString("list_name", item.name)
                        bundle.putString("list_image", item.icon)
                        bundle.putString("list_status", item.status)
                    }


                    intent.putExtras(bundle)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    fun updateClaims(newClaims: List<Claim>) {
        claims = newClaims
        notifyDataSetChanged() // Memperbarui tampilan setelah data klaim diperbarui
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Claim>() {
            override fun areItemsTheSame(oldItem: Claim, newItem: Claim): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Claim, newItem: Claim
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}