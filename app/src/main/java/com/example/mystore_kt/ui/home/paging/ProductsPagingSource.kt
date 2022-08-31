package com.example.mystore_kt.ui.home.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mystore_kt.data.Product
import com.example.mystore_kt.networking.RetrofitInterface
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class ProductsPagingSource(
    private val retrofitInterface: RetrofitInterface,
    private val searchQuery: String
) : PagingSource<Int, Product>() {

    //Responsible for loading data from API using Retrofit
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val currentPage = params.key
            ?: STARTING_PAGE_INDEX // key will be null at the very beginning, so we set it to 1

        return try {
            val products = retrofitInterface.getProducts(currentPage, params.loadSize, searchQuery).data
            LoadResult.Page(
                data = products!!,
                prevKey = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1,
                nextKey = if (products.isEmpty()) null else currentPage + 1
            )
        } catch (exception: IOException) { //No internet connection
            LoadResult.Error(exception)
        } catch (exception: HttpException) { //catches server errors
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return null
    }
}