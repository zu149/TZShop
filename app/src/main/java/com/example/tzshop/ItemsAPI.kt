package com.example.tzshop

import com.example.tzshop.models.Item
import com.example.tzshop.models.Root
import retrofit2.Call
import retrofit2.http.GET



interface ItemsAPI {
    @GET("97e721a7-0a66-4cae-b445-83cc0bcf9010")
    fun getItems(): Call<Root?>?
}

