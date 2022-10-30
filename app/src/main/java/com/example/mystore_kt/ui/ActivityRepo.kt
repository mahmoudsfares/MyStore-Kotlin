package com.example.mystore_kt.ui

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.mystore_kt.R
import com.example.mystore_kt.data.DatastoreManager
import com.example.mystore_kt.data.database.AppDatabase
import com.example.mystore_kt.data.pojo.CartItem
import com.example.mystore_kt.data.pojo.WishlistItem
import com.example.mystore_kt.networking.Resource
import com.example.mystore_kt.networking.RetrofitInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ActivityRepo @Inject constructor(
    private val db: AppDatabase,
    private val dataStoreManager: DatastoreManager,
    private val retrofitInterface: RetrofitInterface,
    @ApplicationContext private val appContext: Context
)
{

    fun getUserId(): Flow<Int?> = dataStoreManager.userId

    fun countItemsInCart(): LiveData<Int> = db.getCartDao().getCartItemsCount()

    /// sync user cart on servers with the local cart
    suspend fun syncCart(cartItem: CartItem?, userId: Int): Flow<Resource<List<CartItem>?>>{
        return flow {
            emit(Resource.Loading())
            cartItem?.userId = userId
            try {
                val listUpdatedCart: List<CartItem> = retrofitInterface.syncCart(listOf(cartItem)).data
                db.getCartDao().clear()
                db.getCartDao().insert(listUpdatedCart)
                emit(Resource.Success(listUpdatedCart))
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> emit(Resource.Error(appContext.getString(R.string.server_error)))
                    is IOException -> emit(Resource.Error(appContext.getString(R.string.no_internet_connection)))
                    else -> emit(
                        Resource.Error(throwable.localizedMessage ?: appContext.getString(
                                R.string.error_loading_data
                            ))
                    )
                }
            }
        }
    }

    /// sync user wishlist on servers with the local wishlist
    suspend fun syncWishlist(wishlistItem: WishlistItem?, userId: Int): Flow<Resource<List<WishlistItem>?>>{
        return flow {
            emit(Resource.Loading())
            wishlistItem?.userId = userId
            try {
                val listUpdatedWishlist: List<WishlistItem> = retrofitInterface.syncWishlist(listOf(wishlistItem)).data
                db.getWishlistDao().clear()
                db.getWishlistDao().insert(listUpdatedWishlist)
                emit(Resource.Success(listUpdatedWishlist))
            } catch (throwable: Throwable) {
                when (throwable) {
                    is HttpException -> emit(Resource.Error(appContext.getString(
                                R.string.server_error
                            )))
                    is IOException -> emit(Resource.Error(appContext.getString(
                                R.string.no_internet_connection
                            )))
                    else -> emit(
                        Resource.Error(throwable.localizedMessage ?: appContext.getString(
                                R.string.error_loading_data
                            ))
                    )
                }
            }
        }
    }

    suspend fun addToCart(cartItem: CartItem) = db.getCartDao().insert(cartItem)

    suspend fun removeFromCart(cartItem: CartItem) = db.getCartDao().delete(cartItem)

    suspend fun changeQuantityInCart(cartItem: CartItem) = db.getCartDao().changeQuantityInCart(cartItem)

    suspend fun clearCart() = db.getCartDao().clear()

    suspend fun addToWishlist(wishlistItem: WishlistItem) = db.getWishlistDao().insert(wishlistItem)

    suspend fun removeFromWishlist(wishlistItem: WishlistItem) = db.getWishlistDao().delete(wishlistItem)

    suspend fun clearWishlist() = db.getWishlistDao().clear()
}