package com.example.mystore_kt.data.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_table")
data class WishlistItem(
    @PrimaryKey
    val id: Int,
    val name: String,
    val mainImageUrl: String?,
    var actionOnItem: ActionOnItem = ActionOnItem.NULL,
    var userId: Int? = null
)
