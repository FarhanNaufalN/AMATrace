package com.example.amatrace.pages.adapter

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.amatrace.pages.supplier.ui.detail.shipping.DetailShippingActivity
import com.example.core.data.source.remote.response.Shipping
import com.example.core.databinding.ItemRowBinding
import com.example.core.databinding.ItemShippingBinding

class ShippingAdapter : PagingDataAdapter<Shipping, ShippingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemShippingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    class MyViewHolder(private val binding: ItemShippingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Shipping) {
            binding.apply {
                productName.text = item.product.name
                productSku.text = item.serialNumberId

                Glide.with(itemView.context).load(item.product.image).into(productImage)

                // Generate and display QR code
                generateQRCode(item.id)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailShippingActivity::class.java)
                    val bundle = Bundle()

                    bundle.putString("shipping_id", item.id)
                    bundle.putString("list_name", item.product.name)
                    bundle.putString("list_image", item.product.image)
                    bundle.putString("list_sku", item.product.sku)

                    intent.putExtras(bundle)
                    itemView.context.startActivity(intent)
                }
            }
        }

        private fun generateQRCode(data: String) {
            val width = itemView.resources.displayMetrics.widthPixels
            val height = itemView.resources.displayMetrics.heightPixels
            val smallerDimension = if (width < height) width else height

            val qrgEncoder = QRGEncoder(data, null, QRGContents.Type.TEXT, smallerDimension / 4)
            qrgEncoder.colorBlack = Color.GREEN
            qrgEncoder.colorWhite = Color.WHITE

            try {
                val bitmap = qrgEncoder.bitmap
                binding.productQRImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Log.v("GenerateQRCode", e.toString())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Shipping>() {
            override fun areItemsTheSame(oldItem: Shipping, newItem: Shipping): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Shipping, newItem: Shipping): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
