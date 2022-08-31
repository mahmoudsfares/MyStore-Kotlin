package com.example.mystore_kt

import android.content.Context
import com.example.mystore_kt.networking.RetrofitInterface
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class HomeTest {

    @Mock private lateinit var context: Context

    private val retrofitInterface: RetrofitInterface = Mockito.mock(RetrofitInterface::class.java)

    @Test
    suspend fun getProductsNoInternet() {
        Mockito.`when`(retrofitInterface.getProducts(1, 10, "")).thenThrow(IOException())

    }
}