package com.example.amatrace.pages.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.amatrace.pages.producer.ui.detail.tambahclaim.UploadSertifikatProducerActivity
import com.example.amatrace.pages.supplier.ui.detail.tambahclaim.UploadSertifikatActivity
import com.example.core.data.source.remote.response.ClaimList
import com.example.core.databinding.ItemRowBinding

class ClaimProductAdapter : PagingDataAdapter<ClaimList, ClaimProductAdapter.MyViewHolder>(
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
        fun bind(item: ClaimList) {

            binding.apply {
                productName.text = item.name
                productSku.text = item.status
                Glide.with(itemView.context).load(item.icon).into(productImage)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, UploadSertifikatProducerActivity::class.java)
                    val bundle = Bundle()

                    bundle.putString("productClaim_id", item.id)
                    bundle.putString("icon", item.icon)
                    bundle.putString("list_name", item.name)

                    intent.putExtras(bundle)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ClaimList>() {
            override fun areItemsTheSame(oldItem: ClaimList, newItem: ClaimList): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ClaimList, newItem:ClaimList
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}