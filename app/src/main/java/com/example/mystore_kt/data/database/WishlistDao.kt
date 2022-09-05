package com.example.mystore_kt.data.database

import androidx.room.*
import com.example.mystore_kt.data.pojo.WishlistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {

    @Query("SELECT * FROM wishlist_table ORDER BY id ASC")
    fun getAllItems(): Flow<List<WishlistItem>>

    @Query("SELECT COUNT() FROM wishlist_table WHERE id = :id")
    fun isItemInWishlist(id: Int): Flow<Boolean>

    @Delete
    suspend fun delete(wishlistItem: WishlistItem)

    @Insert
    suspend fun insert(item: WishlistItem)

    @Insert
    suspend fun insert(items: List<WishlistItem>)

    @Query("DELETE FROM wishlist_table")
    suspend fun clear()
}