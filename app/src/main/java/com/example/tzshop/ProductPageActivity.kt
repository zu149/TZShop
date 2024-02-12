package com.example.tzshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.tzshop.databinding.ActivityProductPageBinding
import com.example.tzshop.models.Item


class ProductPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductPageBinding
    private lateinit var prduct : Item

    private var isDetailsVisible = true // Флаг для отслеживания видимости деталей товара


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_page)

        binding = ActivityProductPageBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.hide.setOnClickListener {
            if (isDetailsVisible) {
                binding.description.visibility = View.GONE
                binding.transfer.visibility = View.GONE
                binding.hide.text = getString(R.string.more)
            } else {
                binding.description.visibility = View.VISIBLE
                binding.transfer.visibility = View.VISIBLE
                binding.hide.text = getString(R.string.hide)
            }
            isDetailsVisible = !isDetailsVisible
        }

        binding.more.setOnClickListener {
            if (isDetailsVisible) {
                binding.ingredients.visibility = View.GONE
                binding.more.text = getString(R.string.more)
            } else {
                binding.ingredients.visibility = View.VISIBLE
                binding.more.text = getString(R.string.hide)
            }
            isDetailsVisible = !isDetailsVisible
        }



        if (intent.hasExtra("item")) {
            prduct = intent.getParcelableExtra("item")!! //прием заполненного объекта
        }

        GlideLoader(this).loadImage(this.getResources().getIdentifier("a${prduct.id.take(8)}", "drawable", this.getPackageName()),binding.image)
        binding.titleCard.text = prduct.title
        binding.subtitle.text = prduct.subtitle
        binding.description.text = prduct.description
        binding.available.text = "Доступно для заказа " + prduct.available.toString()
        binding.star.rating = prduct.feedback.rating.toFloat()
        binding.rating.text = prduct.feedback.rating.toString()
        binding.count.text = prduct.feedback.count.toString() + " отзыва"
        binding.priceSale.text = prduct.price.priceWithDiscount + " ₽"
        binding.price.text = prduct.price.price + " ₽"
        binding.discount.text = prduct.price.discount.toString() + " %"
        binding.titleCard2.text = prduct.title
        val x = prduct.info[0].toString()
        binding.title.text = prduct.info[0].title
        binding.label.text = prduct.info[0].value
        binding.title2.text = prduct.info[1].title
        binding.label2.text = prduct.info[1].value
        binding.title3.text = prduct.info[1].title
        binding.label3.text = prduct.info[1].value
        binding.ingredients.text = prduct.ingredients

        setupActionBar()
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