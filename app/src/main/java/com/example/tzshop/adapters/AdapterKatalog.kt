package com.example.tzshop.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tzshop.GlideLoader
import com.example.tzshop.R
import com.example.tzshop.databinding.ProductBinding
import com.example.tzshop.models.Item
import com.google.gson.Gson
import com.google.common.reflect.TypeToken
import java.lang.reflect.Type

class AdapterKatalog(val context: FragmentActivity, val list: List<Item>): RecyclerView.Adapter<AdapterKatalog.ViewHolder>() {

    var click : OnClickListener? = null
    fun setOnClick (onClickListener: OnClickListener) {
        this.click = onClickListener
    }
    interface OnClickListener {
        fun onClick (position: Int, item: Item) {

        }
    }

    class ViewHolder( val binding: ProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product, parent, false)
        return ViewHolder(ProductBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: Item = list[position]
        holder.binding.icon.setOnClickListener {
            val preferences = context.getSharedPreferences(
                "productpreferences",
                Context.MODE_PRIVATE
            )
            var liked = false
            val gson = Gson()
            val json = preferences.getString("productgson", null)
            val type: Type = object : TypeToken<ArrayList<Item?>?>() {}.type
            val list: ArrayList<Item> = gson.fromJson<Any>(json, type) as ArrayList<Item>
            for (i in list) {
                if (i.id == (data.id)) {
                    holder.binding.icon.setBackgroundResource(R.drawable.like)
                    liked = true
                }
            }
            holder.binding.icon.setOnClickListener {
                if (liked) {
                    holder.binding.icon.setBackgroundResource(R.drawable.favourites)
                    val edit = preferences.edit()
                    list.remove(data)
                    val gsonData: String = Gson().toJson(list)
                    edit.putString("productgson", gsonData)
                    edit.apply()
                } else {
                    val edit = preferences.edit()
                    list.add(data)
                    val gsonData: String = Gson().toJson(list)
                    edit.putString("productgson", gsonData)
                    edit.apply()
                    holder.binding.icon.setBackgroundResource(R.drawable.like)
                }
            }
        }

        holder.binding.title.text = data.title
        holder.binding.price.text = data.price.price + "₽"
        holder.binding.discount.text = "-${data.price.discount.toString() +"%"}"
        holder.binding.priceWithDiscount.text = data.price.priceWithDiscount + "₽"
        holder.binding.description.text = data.description
        holder.binding.rating.text = data.feedback.rating.toString()
        holder.binding.count.text = data.feedback.count.toString()
        GlideLoader(context).loadImage(context.getResources().getIdentifier("a${data.id.take(8)}", "drawable", context.getPackageName()),holder.binding.image)


        holder.itemView.setOnClickListener{
            if (click != null) {
                click!!.onClick(position, data)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}