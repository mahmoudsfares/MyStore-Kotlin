package com.example.mystore_kt.data.pojo

import com.google.gson.annotations.SerializedName

enum class ActionOnItem {
    @SerializedName("0")
    NULL,

    @SerializedName("1")
    ADD,

    @SerializedName("2")
    REMOVE,
}