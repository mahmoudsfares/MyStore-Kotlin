package com.example.mystore_kt.data.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartItem(
    @PrimaryKey
    val id: Int,
    val name: String,
    val mainImageUrl: String?,
    var quantity: Int = 1,
    var actionOnItem: ActionOnItem = ActionOnItem.NULL,
    var userId: Int? = null
)