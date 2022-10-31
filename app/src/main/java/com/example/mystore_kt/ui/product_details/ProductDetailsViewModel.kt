package com.example.mystore_kt.ui.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    // we won't trigger the flow multiple times in this case but the channel was used because we need to pass data
    private val isItemInCartTriggerChannel = Channel<Int>()
    fun checkItemInCart(itemId: Int) {
        viewModelScope.launch {
            isItemInCartTriggerChannel.send(itemId)
        }
    }
    private val isItemInCartFlow = isItemInCartTriggerChannel.receiveAsFlow()
    val isItemInCart: StateFlow<Boolean?> = isItemInCartFlow.flatMapLatest { id ->
        repo.isItemInCart(id)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)


    private val isItemInFavouritesTriggerChannel = Channel<Int>()
    fun checkItemInFavourites(itemId: Int) {
        viewModelScope.launch {
            isItemInFavouritesTriggerChannel.send(itemId)
        }
    }
    private val isItemInFavouritesFlow = isItemInFavouritesTriggerChannel.receiveAsFlow()
    val isItemInWishlist: StateFlow<Boolean?> = isItemInFavouritesFlow.flatMapLatest { id ->
            repo.isItemInWishlist(id)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)


    //TODO: remove this when the backend is complete
    fun test(){
        _productDetails.value = Resource.Success(DetailedProduct(1, "iPhone 11", "really good phone", "u", listOf("u"), 29.5))
        checkItemInCart(1)
        checkItemInFavourites(1)
    }
}