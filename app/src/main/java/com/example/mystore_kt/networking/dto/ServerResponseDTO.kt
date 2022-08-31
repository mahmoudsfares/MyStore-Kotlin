package com.example.mystore_kt.networking.dto
import com.google.gson.annotations.SerializedName

data class ServerResponseDTO<T>(
    @SerializedName("error")
    val error: String?,
    @SerializedName("data")
    val data: T?
)