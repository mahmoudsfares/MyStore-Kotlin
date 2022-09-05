package com.example.mystore_kt.networking

import com.example.mystore_kt.data.pojo.DetailedProduct
import com.example.mystore_kt.data.pojo.Product
import com.example.mystore_kt.networking.dto.ServerResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("products")
    suspend fun getProducts(@Query("page") page: Int, @Query("pageSize") pageSize: Int, @Query("q") searchQuery: String): ServerResponseDTO<List<Product>>

    @GET("product/{id}")
    suspend fun getProductDetails(@Path("id") id: Int): ServerResponseDTO<DetailedProduct>
}