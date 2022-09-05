package com.example.mystore_kt.ui.product_details

import android.content.Context
import com.example.mystore_kt.R
import com.example.mystore_kt.data.pojo.DetailedProduct
import com.example.mystore_kt.networking.Resource
import com.example.mystore_kt.networking.RetrofitInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProductDetailsRepo @Inject constructor(
    private val retrofitInterface: RetrofitInterface,
    @ApplicationContext private val context: Context
){
    suspend fun getProductDetails(id: Int): Resource<DetailedProduct> {
        return try {
            val dto = retrofitInterface.getProductDetails(id)
            if (dto.error == null) Resource.Success(dto.data)
            else {
                Resource.Error(dto.error)
            }
        } catch (th: Throwable){
            when(th){
                is IOException -> Resource.Error(context.getString(R.string.no_internet_connection))
                is HttpException -> Resource.Error(context.getString(R.string.server_error))
                else -> Resource.Error(context.getString(R.string.unexpected_error))
            }
        }
    }
}