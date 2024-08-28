package com.example.healthcare.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.healthcare.Models.SliderItems
import com.example.healthcare.R

class SliderAdapter(
    private val sliderItems: MutableList<SliderItems>,
    private val viewPager2: ViewPager2
) : RecyclerView.Adapter<SliderAdapter.Sliderviewholder>() {

    private lateinit var context: Context
    private val runnable = Runnable {
        sliderItems.addAll(sliderItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Sliderviewholder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slider_viewholder, parent, false)
        return Sliderviewholder(view)
    }

    override fun onBindViewHolder(holder: Sliderviewholder, position: Int) {
        val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(60))

        Glide.with(context)
            .load(sliderItems[position].image)
            .apply(requestOptions)
            .into(holder.imageView)

        if (position == sliderItems.size - 2) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class Sliderviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageSlide)
    }
}
