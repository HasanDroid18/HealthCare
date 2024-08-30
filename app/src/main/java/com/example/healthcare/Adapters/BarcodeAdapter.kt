package com.example.healthcare.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcare.Models.BarcodeData
import android.content.Context
import android.view.View
import android.widget.TextView
import com.example.healthcare.R

class BarcodeAdapter(private val context: Context, private val barcodeItems: List<BarcodeData>) :
    RecyclerView.Adapter<BarcodeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNumber: TextView = itemView.findViewById(R.id.counter)
        val textViewType: TextView = itemView.findViewById(R.id.text_view_qr_type)
        val textViewContent: TextView = itemView.findViewById(R.id.text_view_qr_content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.barcode_data_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = barcodeItems[position]
        holder.textViewNumber.text = (position + 1).toString() // Numbering starts from 1
        holder.textViewType.text = item.type
        holder.textViewContent.text = item.content
    }

    override fun getItemCount(): Int = barcodeItems.size
}
