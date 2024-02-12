package com.example.tzshop

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tzshop.adapters.AdapterFavourites
import com.example.tzshop.databinding.ActivityFavouritesBinding
import com.example.tzshop.models.Item
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

class FavouritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()

        getlist()

    }

    private fun getlist() {
        val preferences = getSharedPreferences(
            "productpreferences",
            Context.MODE_PRIVATE
        )
        val gson = Gson()
        val json = preferences.getString("productgson", null)
        val type: Type = object : TypeToken<ArrayList<Item?>?>() {}.type
        val list: List<Item> = gson.fromJson<Any>(json, type) as ArrayList<Item>

        if (list.isNotEmpty()) {
            binding.spisok.layoutManager = GridLayoutManager(this, 2) //GridLayoutManager - сетка
            binding.spisok.setHasFixedSize(true) // не будет искажаться, когда будет изменяться количество элементов
            val x = AdapterFavourites(this, list) // x содержить значиения адапетра
            binding.spisok.adapter = x
        }
    }


    private fun setupActionBar() {
        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_arrow)

        }
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }



}