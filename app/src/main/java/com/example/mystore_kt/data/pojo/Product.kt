package com.example.mystore_kt.data.pojo

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("mainImageUrl") val mainImageUrl: String,
    @SerializedName("price") val price: Double,
)