package com.example.mystore_kt.ui.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystore_kt.data.pojo.DetailedProduct
import com.example.mystore_kt.networking.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProductDetailsViewModel @Inject constructor(private val repo: ProductDetailsRepo): ViewModel() {

    private val _productDetails = MutableStateFlow<Resource<DetailedProduct>>(Resource.Loading())
    val productDetails: StateFlow<Resource<DetailedProduct>>
        get() = _productDetails

    fun getProductDetails(id: Int){
        _productDetails.value = Resource.Loading()
        viewModelScope.launch {
            _productDetails.value = repo.getProductDetails(id)
        }
    }
}