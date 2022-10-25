package com.example.mystore_kt.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystore_kt.data.database.AppDatabase
import com.example.mystore_kt.data.pojo.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor (db: AppDatabase) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>?> = db.getCartDao().getAllItems().stateIn(viewModelScope, SharingStarted.Lazily, null)
}