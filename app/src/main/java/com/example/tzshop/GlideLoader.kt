package com.example.tzshop

import android.content.Context
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.io.IOException


class GlideLoader(val context: Context) : AppCompatActivity() {

    fun loadImage(image: Any, imageView: ImageView) {
        try {
            Glide // изображение загружается по ссылке в имэдж вью
                .with(context)
                .load(image)
                .centerCrop()
                .into(imageView) //загрузка в imageView
        }catch (e : IOException) {
            e.printStackTrace()
        }
    }

}