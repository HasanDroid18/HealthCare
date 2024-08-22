package com.example.healthcare.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcare.Models.HospitalItem
import com.example.healthcare.R

class HospitalAdapter(private var hospitalList: List<HospitalItem>) : RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder>() {

    class HospitalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val hospitalName : TextView = itemView.findViewById(R.id.hospital_name)
        val hospitalType : TextView = itemView.findViewById(R.id.hospital_type)
        val hospitalStatus : TextView = itemView.findViewById(R.id.hospital_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalAdapter.HospitalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.hospital_item, parent, false)
        return HospitalViewHolder(view)
    }

    override fun onBindViewHolder(holder: HospitalAdapter.HospitalViewHolder, position: Int) {
        val currentItem = hospitalList[position]
        holder.hospitalName.text = currentItem.name
        holder.hospitalType.text = currentItem.type
        holder.hospitalStatus.text = currentItem.status
    }

    fun updateData(newData: List<HospitalItem>) {
        hospitalList = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return hospitalList.size
    }
}