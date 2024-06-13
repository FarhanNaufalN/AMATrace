package com.example.amatrace.pages.consumer.ui.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.amatrace.R
import com.example.core.data.source.remote.response.RawClaim
import com.example.core.data.source.remote.response.rawProductUsageMonthly

class RawForecastDetailAdapter(
    var claimList: List<rawProductUsageMonthly>
) : RecyclerView.Adapter<RawForecastDetailAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val claimTitle: TextView = itemView.findViewById(R.id.productName)
        private val claimDescription: TextView = itemView.findViewById(com.example.core.R.id.banyak_terpakai)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(claim: rawProductUsageMonthly) {
            claimTitle.text = claim.month
            claimDescription.text = claim.totalUsage
        }

        override fun onClick(v: View?) {
            // Handle click event if needed
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.example.core.R.layout.item_rawprodukforecast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val claim = claimList[position]
        holder.bind(claim)
    }

    override fun getItemCount(): Int {
        return claimList.size
    }
}
