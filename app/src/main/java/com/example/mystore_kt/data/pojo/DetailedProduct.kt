package com.example.mystore_kt.data.pojo

import com.google.gson.annotations.SerializedName

class DetailedProduct (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("mainImageUrl") val mainImageUrl: String,
    @SerializedName("imageUrls") val imageUrls: List<String>,
    @SerializedName("price") val price: Double,
)