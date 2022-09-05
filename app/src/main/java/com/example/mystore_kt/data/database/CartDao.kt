package com.example.mystore_kt.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mystore_kt.data.pojo.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_table ORDER BY id ASC")
    fun getAllItems(): Flow<List<CartItem>>

    @Query("SELECT COUNT() FROM cart_table")
    fun getCartItemsCount(): LiveData<Int>

    @Query("SELECT COUNT() FROM cart_table WHERE id = :id")
    fun isItemInCart(id: Int): Flow<Boolean>

    @Insert
    suspend fun insert(item: CartItem)

    @Insert
    suspend fun insert(items: List<CartItem>)

    @Update
    suspend fun changeQuantityInCart(cartItem: CartItem)

    @Delete
    suspend fun delete(cartItem: CartItem)

    @Query("DELETE FROM cart_table")
    suspend fun clear()
}