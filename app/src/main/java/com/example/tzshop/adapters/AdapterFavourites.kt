package com.example.tzshop.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tzshop.FavouritesActivity
import com.example.tzshop.GlideLoader
import com.example.tzshop.R
import com.example.tzshop.databinding.ProductBinding
import com.example.tzshop.models.Item
import com.google.gson.Gson

class AdapterFavourites (val context: FavouritesActivity, val list: List<Item>): RecyclerView.Adapter<AdapterFavourites.ViewHolder>() {


    class ViewHolder( val binding: ProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product, parent, false)
        return ViewHolder(ProductBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: Item = list[position]
        holder.binding.icon.setOnClickListener{

            val preferences = context.getSharedPreferences("productpreferences",
                Context.MODE_PRIVATE
            )
            val edit = preferences.edit()
            val list = ArrayList<Item>()
            list.add(data)
            val gsonData : String = Gson().toJson(list)
            edit.putString("productgson", gsonData)
            edit.apply()
            holder.binding.icon.setBackgroundColor(0xFFEC407A.toInt())
        }
        holder.binding.title.text = data.title
        holder.binding.price.text = data.price.price + "₽"
        holder.binding.discount.text = "-${data.price.discount.toString() +"%"}"
        holder.binding.priceWithDiscount.text = data.price.priceWithDiscount + "₽"
        holder.binding.description.text = data.description
        holder.binding.rating.text = data.feedback.rating.toString()
        holder.binding.count.text = data.feedback.count.toString()
        GlideLoader(context).loadImage(context.getResources().getIdentifier("a${data.id.take(8)}", "drawable", context.getPackageName()),holder.binding.image)


    }

    override fun getItemCount(): Int {
        return list.size
    }

}