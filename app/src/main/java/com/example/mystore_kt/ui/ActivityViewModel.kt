package com.example.mystore_kt.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystore_kt.data.pojo.ActionOnItem
import com.example.mystore_kt.data.pojo.CartItem
import com.example.mystore_kt.data.pojo.WishlistItem
import com.example.mystore_kt.networking.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ActivityViewModel @Inject constructor(private val repo: ActivityRepo) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(3000)
            _isLoading.value = false
        }
    }

    private val userIdFlow: StateFlow<Int?> =
        repo.getUserId().stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val userId: Int?
        get() {
            return userIdFlow.value
        }

    //----------------- CART -----------------//

    val cartItemsCount = repo.countItemsInCart()

    private val syncCartTriggerChannel = Channel<CartItem?>()
    private val syncCartTrigger = syncCartTriggerChannel.receiveAsFlow()

    val syncCartStatus: Flow<Resource<List<CartItem>?>?> =
        syncCartTrigger.flatMapLatest {
            repo.syncCart(it, userId!!)
        }


    fun syncCart(cartItem: CartItem?) {
        if (userId != null) {
            viewModelScope.launch {
                syncCartTriggerChannel.send(cartItem)
            }
        }
    }

    fun addToCart(cartItem: CartItem) {
        cartItem.actionOnItem = ActionOnItem.ADD
        if (userId == null) {
            viewModelScope.launch {
                repo.addToCart(cartItem)
            }
        } else {
            syncCart(cartItem)
        }
    }

    fun removeFromCart(cartItem: CartItem) {
        cartItem.actionOnItem = ActionOnItem.REMOVE
        if (userId == null) {
            viewModelScope.launch {
                repo.removeFromCart(cartItem)
            }
        } else {
            syncCart(cartItem)
        }
    }

    fun changeQuantityInCart(cartItem: CartItem) {
        cartItem.actionOnItem = ActionOnItem.ADD
        if (userId == null) {
            viewModelScope.launch {
                repo.changeQuantityInCart(cartItem)
            }
        } else {
            syncCart(cartItem)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repo.clearCart()
        }
    }

    //----------------- WISHLIST -----------------//

    private val syncFavouritesTriggerChannel = Channel<WishlistItem?>()
    private val syncFavouritesTrigger = syncFavouritesTriggerChannel.receiveAsFlow()

    val syncFavouritesStatus: Flow<Resource<List<WishlistItem>?>?> =
        syncFavouritesTrigger.flatMapLatest {
            repo.syncWishlist(it, userId!!)
        }

    fun syncWishlist(wishlistItem: WishlistItem) {
        if (userId != null) {
            viewModelScope.launch {
                syncFavouritesTriggerChannel.send(wishlistItem)
            }
        }
    }

    fun addToFavourites(wishlistItem: WishlistItem) {
        wishlistItem.actionOnItem = ActionOnItem.ADD
        if (userId == null) {
            viewModelScope.launch {
                repo.addToWishlist(wishlistItem)
            }
        } else {
            syncWishlist(wishlistItem)
        }
    }

    fun removeFromWishlist(wishlistItem: WishlistItem) {
        wishlistItem.actionOnItem = ActionOnItem.REMOVE
        if (userId == null) {
            viewModelScope.launch {
                repo.removeFromWishlist(wishlistItem)
            }
        } else {
            syncWishlist(wishlistItem)
        }
    }

    fun clearWishlist() {
        viewModelScope.launch {
            repo.clearWishlist()
        }
    }
}