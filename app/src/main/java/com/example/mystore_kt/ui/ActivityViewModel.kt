package com.example.mystore_kt.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystore_kt.data.pojo.ActionOnItem
import com.example.mystore_kt.data.pojo.CartItem
import com.example.mystore_kt.data.pojo.WishlistItem
import com.example.mystore_kt.networking.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(private val repo: ActivityRepo) : ViewModel() {

    // this is used for splash screen
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    init {
        viewModelScope.launch {
            delay(3000)
            _isLoading.value = false
        }
    }
    //--------- end-splash ---------//


    // retrieved as a flow because it's saved in the datastore, logout and login will be observed
    private val _userId: StateFlow<Int?> = repo.getUserId().stateIn(viewModelScope, SharingStarted.Lazily, null)
    val userId: Int?
        get() {
            return _userId.value
        }
    //--------- end-user ---------//


    //----------------- CART -----------------//
    // this doesn't need to be triggered except once when the app starts
    val cartItemsCount = repo.countItemsInCart()

    // used as a trigger because cart is synced from different places and is also refreshed
    private val syncCartTriggerChannel = Channel<CartItem?>()
    /// triggers the flow and passes the repo method parameter (cart item)
    fun syncCart(cartItem: CartItem?) {
        if (userId != null) {
            viewModelScope.launch {
                syncCartTriggerChannel.send(cartItem)
            }
        }
    }
    // convert the channel to flow
    private val syncCartFlow = syncCartTriggerChannel.receiveAsFlow()
    // the state flow that is observed and stores the hottest value of the flow
    // stateIn: converts the cold flow to hot state flow
    val syncCartStatus: StateFlow<Resource<List<CartItem>?>?> =
        syncCartFlow.flatMapLatest {
            repo.syncCart(it, userId!!)
        }.stateIn(viewModelScope, SharingStarted.Lazily, null)


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
    private fun syncWishlist(wishlistItem: WishlistItem) {
        if (userId != null) {
            viewModelScope.launch {
                syncFavouritesTriggerChannel.send(wishlistItem)
            }
        }
    }
    private val syncFavouritesFlow = syncFavouritesTriggerChannel.receiveAsFlow()
    val syncWishlistStatus: Flow<Resource<List<WishlistItem>?>?> =
        syncFavouritesFlow.flatMapLatest {
            repo.syncWishlist(it, userId!!)
        }


    fun addToWishlist(wishlistItem: WishlistItem) {
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