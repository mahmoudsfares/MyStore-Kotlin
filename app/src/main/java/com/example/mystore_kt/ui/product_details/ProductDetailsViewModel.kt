package com.example.mystore_kt.ui.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystore_kt.data.pojo.ActionOnItem
import com.example.mystore_kt.data.pojo.CartItem
import com.example.mystore_kt.data.pojo.DetailedProduct
import com.example.mystore_kt.networking.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
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

    private val isItemInCartChannel = Channel<Int>()
    private val isItemInCartTrigger = isItemInCartChannel.receiveAsFlow()
    val isItemInCart: StateFlow<Boolean?> = isItemInCartTrigger.flatMapLatest { id ->
        repo.isItemInCart(id)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun startIsItemInCartFlow(itemId: Int) {
        viewModelScope.launch {
            isItemInCartChannel.send(itemId)
        }
    }

    private val isItemInFavouritesChannel = Channel<Int>()
    private val isItemInFavouritesTrigger = isItemInFavouritesChannel.receiveAsFlow()
    val isItemInWishlist: StateFlow<Boolean?> =
        isItemInFavouritesTrigger.flatMapLatest { id ->
            repo.isItemInWishlist(id)
        }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun startIsItemInFavouritesFlow(itemId: Int) {
        viewModelScope.launch {
            isItemInFavouritesChannel.send(itemId)
        }
    }
}