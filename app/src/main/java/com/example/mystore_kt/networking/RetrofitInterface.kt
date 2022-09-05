package com.example.mystore_kt.networking

import com.example.mystore_kt.data.pojo.CartItem
import com.example.mystore_kt.data.pojo.DetailedProduct
import com.example.mystore_kt.data.pojo.Product
import com.example.mystore_kt.data.pojo.WishlistItem
import com.example.mystore_kt.networking.dto.ServerResponseDTO
import retrofit2.http.*

interface RetrofitInterface {
    @GET("products")
    suspend fun getProducts(@Query("page") page: Int, @Query("pageSize") pageSize: Int, @Query("q") searchQuery: String): ServerResponseDTO<List<Product>>

    @GET("product/{id}")
    suspend fun getProductDetails(@Path("id") id: Int): ServerResponseDTO<DetailedProduct>

    @POST("syncCart")
    suspend fun syncCart(@Body localCartItems: List<CartItem?>): ServerResponseDTO<List<CartItem>>

    @POST("syncWishlist")
    suspend fun syncWishlist(@Body localFavouriteItems: List<WishlistItem?>): ServerResponseDTO<List<WishlistItem>>
}