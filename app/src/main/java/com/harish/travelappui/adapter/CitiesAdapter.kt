package com.harish.travelappui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harish.travelappui.R
import com.harish.travelappui.models.CitiesResponse
import kotlinx.android.synthetic.main.city_row.view.*

class CitiesAdapter(val citiesResponse:CitiesResponse):RecyclerView.Adapter<CitiesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount()=citiesResponse.cities.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = citiesResponse.cities[position]
        holder.itemView.apply {
            Glide.with(context).load(city.imagePath).into(city_image)
            city_desc.text = city.subTitle
            city_title.text = city.title
        }
    }
}