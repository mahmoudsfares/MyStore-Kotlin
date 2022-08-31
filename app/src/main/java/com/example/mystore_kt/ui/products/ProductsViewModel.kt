package com.example.mystore_kt.ui.products

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.mystore_kt.networking.RetrofitInterface
import com.example.mystore_kt.ui.products.paging.ProductsPagingSource
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val PAGE_SIZE = 10
private const val MAX_SIZE = 30

@HiltViewModel
class ProductsViewModel @Inject constructor(private val retrofitInterface: RetrofitInterface) : ViewModel(){

    private val productsChannel = Channel<Boolean>()
    private val productsFlow = productsChannel.receiveAsFlow()
    private var searchQuery = ""

    val products = productsFlow.flatMapLatest {
        Pager(
            config = PagingConfig(PAGE_SIZE, MAX_SIZE, false),
            pagingSourceFactory = { ProductsPagingSource(retrofitInterface, searchQuery) }
        ).flow.cachedIn(viewModelScope)
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun getProducts(searchQuery: String) {
        this.searchQuery = searchQuery
        viewModelScope.launch {
            productsChannel.send(true)
        }
    }

}