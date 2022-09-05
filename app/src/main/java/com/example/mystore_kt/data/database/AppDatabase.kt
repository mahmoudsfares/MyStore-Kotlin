package com.example.mystore_kt.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mystore_kt.data.pojo.CartItem
import com.example.mystore_kt.data.pojo.WishlistItem


@Database(
    entities = [CartItem::class, WishlistItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCartDao(): CartDao
    abstract fun getWishlistDao(): WishlistDao
}
