package com.example.amatrace.pages.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.amatrace.R
import com.example.amatrace.pages.consumer.ui.detail.klaim.DetailKlaimActivity
import com.example.core.data.source.remote.preferences.Preference
import com.example.core.data.source.remote.response.ConsumerClaim

class ConsumerClaimDetailAdapter(
    private val context: Context,
    var claimList: List<ConsumerClaim>,
    private var myPreference: Preference
) : RecyclerView.Adapter<ConsumerClaimDetailAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val claimTitle: TextView = itemView.findViewById(R.id.productName)
        val claimDescription: TextView = itemView.findViewById(R.id.productSku)
        val claimImage: ImageView = itemView.findViewById(R.id.productImage)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(claim: ConsumerClaim) {
            claimTitle.text = claim.title
            claimDescription.text = claim.status
            Glide.with(itemView.context).load(claim.icon).into(claimImage)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val productId = myPreference.getConsumerScanDetail()?.product?.id
            if (position != RecyclerView.NO_POSITION) {
                val intent = Intent(context, DetailKlaimActivity::class.java).apply {
                    putExtra("PRODUCT_ID", productId)
                    putExtra("PRODUCT_CLAIM_ID", claimList[position].id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.core.R.layout.item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val claim = claimList[position]
        holder.bind(claim)
    }

    override fun getItemCount() = claimList.size
}
