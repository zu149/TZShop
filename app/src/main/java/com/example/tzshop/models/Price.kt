package com.example.tzshop.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Price(
    val discount: Int,
    val price: String,
    val priceWithDiscount: String,
    val unit: String
): Parcelable