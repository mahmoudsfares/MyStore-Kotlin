package com.example.mystore_kt.product_details

import com.example.mystore_kt.data.DetailedProduct
import com.example.mystore_kt.networking.RetrofitInterface
import com.example.mystore_kt.ui.product_details.ProductDetailsRepo
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.*
import javax.inject.Inject

class ProductDetailsTest @Inject constructor(private val repo: ProductDetailsRepo) {

    private val dataSource = mock(RetrofitInterface::class.java)

    @Test
    fun getProductDetailsCorrect() = runBlocking {
        `when`(dataSource.getProductDetails(any())).thenAnswer {
            DetailedProduct(1, "iPhone 11", "Nice phone", "apple.com/img.png", listOf(),29.5)
        }
        val detailedProductState = repo.getProductDetails(1)
        Assert.assertEquals(detailedProductState.data!!.id, 1)
    }


}